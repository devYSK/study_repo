/************************************
일별 세션건수, 일별 방문 사용자(유저), 사용자별 평균 세션 수
*************************************/
with temp_01 as 
(
	select to_char(date_trunc('day', visit_stime), 'yyyy-mm-dd') as d_day
		-- ga_sess 테이블에는 sess_id로 unique하므로 count(sess_id)와 동일
		, count(distinct sess_id) as daily_sess_cnt
		, count(sess_id) as daily_sess_cnt_again
		, count(distinct user_id) as daily_user_cnt 
	from ga.ga_sess group by to_char(date_trunc('day', visit_stime), 'yyyy-mm-dd')
)
select * 
	, 1.0*daily_sess_cnt/daily_user_cnt as avg_user_sessions
	-- 아래와 같이 정수와 정수를 나눌 때 postgresql은 정수로 형변환 함. 1.0을 곱해주거나 명시적으로 float type선언 
	--, daily_sess_cnt/daily_user_cnt
from temp_01;

/************************************
DAU, WAU, MAU 구하기
*************************************/
/* 아래는 이미 많은 과거 데이터가 있을 경우를 가정하고 DAU, WAU, MAU를 추출함 */

-- 일별 방문한 고객 수(DAU)
select date_trunc('day', visit_stime)::date as d_day, count(distinct user_id) as user_cnt 
from ga.ga_sess 
--where visit_stime between to_date('2016-10-25', 'yyyy-mm-dd') and to_timestamp('2016-10-31 23:59:59', 'yyyy-mm-dd hh24:mi:ss')
group by date_trunc('day', visit_stime)::date;

-- 주별 방문한 고객수(WAU)
select date_trunc('week', visit_stime)::date as week_d기y, count(distinct user_id) as user_cnt
from ga.ga_sess
--where visit_stime between to_date('2016-10-24', 'yyyy-mm-dd') and to_timestamp('2016-10-31 23:59:59', 'yyyy-mm-dd hh24:mi:ss')
group by date_trunc('week', visit_stime)::date order by 1;

-- 월별 방문한 고객수(MAU)
select date_trunc('month', visit_stime)::date as month_day, count(distinct user_id) as user_cnt 
from ga.ga_sess 
--where visit_stime between to_date('2016-10-2', 'yyyy-mm-dd') and to_timestamp('2016-10-31 23:59:59', 'yyyy-mm-dd hh24:mi:ss')
group by date_trunc('month', visit_stime)::date;

/* 아래는 하루 주기로 계속 DAU, WAU(이전 7일), MAU(이전 30일)를 계속 추출. */

-- interval로 전일 7일 구하기
select to_date('20161101', 'yyyymmdd') - interval '7 days';

-- 현재 일을 기준으로 전 7일의 WAU 구하기
select :current_date, count(distinct user_id) as wau
from ga_sess
where visit_stime >= (:current_date - interval '7 days') and visit_stime < :current_date;

-- 현재 일을 기준으로 전일의 DAU 구하기
select :current_date, count(distinct user_id) as dau
from ga_sess
where visit_stime >= (:current_date - interval '1 days') and visit_stime < :current_date;

-- 날짜별로 DAU, WAU, MAU 값을 가지는 테이블 생성. 
create table if not exists daily_acquisitions
(d_day date,
dau integer,
wau integer,
mau integer
);

--daily_acquisitions 테이블에 지정된 current_date별 DAU, WAU, MAU을 입력
insert into daily_acquisitions
select 
	:current_date, 
	-- scalar subquery는 select 절에 사용가능하면 단 한건, 한 컬럼만 추출되어야 함. 
	(select count(distinct user_id) as dau
	from ga_sess
	where visit_stime >= (:current_date - interval '1 days') and visit_stime < :current_date
	),
	(select count(distinct user_id) as wau
	from ga_sess
	where visit_stime >= (:current_date - interval '7 days') and visit_stime < :current_date
	),
	(select count(distinct user_id) as mau
	from ga_sess
	where visit_stime >= (:current_date - interval '30 days') and visit_stime < :current_date
	)
;
-- 데이터 입력 확인. 
select * from daily_acquisitions;


-- 과거 일자별로 DAU 생성
with 
temp_00 as (
select generate_series('2016-08-02'::date , '2016-11-01'::date, '1 day'::interval)::date as current_date
)
select b.current_date, count(distinct user_id) as dau
from ga_sess a
	cross join temp_00 b
where visit_stime >= (b.current_date - interval '1 days') and visit_stime < b.current_date
group by b.current_date;

-- 과거 일자별로 지난 7일 WAU 생성. 
with 
temp_00 as (
select generate_series('2016-08-02'::date , '2016-11-01'::date, '1 day'::interval)::date as current_date
)
select b.current_date, count(distinct user_id) as wau
from ga_sess a
	cross join temp_00 b
where visit_stime >= (b.current_date - interval '7 days') and visit_stime < b.current_date
group by b.current_date;

-- 과거 일자별로 지난 30일의 MAU 설정. 
with 
temp_00 as (
select generate_series('2016-08-02'::date , '2016-11-01'::date, '1 day'::interval)::date as current_date
)
select b.current_date, count(distinct user_id) as mau
from ga_sess a
	cross join temp_00 b
where visit_stime >= (b.current_date - interval '30 days') and visit_stime < b.current_date
group by b.current_date;


--데이터 확인 81587, 80693, 80082
select count(distinct user_id) as mau
	from ga_sess
	where visit_stime >= (:current_date - interval '30 days') and visit_stime < :current_date;


-- 과거 일자별로 DAU 생성하는 임시 테이블 생성
drop table if exists daily_dau;

create table daily_dau
as
with 
temp_00 as (
select generate_series('2016-08-02'::date , '2016-11-01'::date, '1 day'::interval)::date as current_date
)
select b.current_date, count(distinct user_id) as dau
from ga_sess a
	cross join temp_00 b
where visit_stime >= (b.current_date - interval '1 days') and visit_stime < b.current_date
group by b.current_date
;

-- 과거 일자별로 WAU 생성하는 임시 테이블 생성
drop table if exists daily_wau;

create table daily_wau
as
with 
temp_00 as (
select generate_series('2016-08-02'::date , '2016-11-01'::date, '1 day'::interval)::date as current_date
)
select b.current_date, count(distinct user_id) as wau
from ga_sess a
	cross join temp_00 b
where visit_stime >= (b.current_date - interval '7 days') and visit_stime < b.current_date
group by b.current_date;

-- 과거 일자별로 MAU 생성하는 임시 테이블 생성
drop table if exists daily_mau;

create table daily_mau
as
with 
temp_00 as (
select generate_series('2016-08-02'::date , '2016-11-01'::date, '1 day'::interval)::date as current_date
)
select b.current_date, count(distinct user_id) as mau
from ga_sess a
	cross join temp_00 b
where visit_stime >= (b.current_date - interval '30 days') and visit_stime < b.current_date
group by b.current_date;

-- DAU, WAU, MAU 임시테이블을 일자별로 조인하여 daily_acquisitions 테이블 생성. 
drop table if exists daily_acquisitions;

create table daily_acquisitions
as
select a.current_date, a.dau, b.wau, c.mau
from daily_dau a
	join daily_wau b on a.current_date = b.current_date
	join daily_mau c on a.current_date = c.current_date
;

select * from daily_acquisitions;

drop table if exists daily_acquisitions;

-- 아래와 같이 current_date 컬럼명을 curr_date로 수정합니다. 
create table daily_acquisitions
as
select a.current_date as curr_date, a.dau, b.wau, c.mau
from daily_dau a
	join daily_wau b on a.current_date = b.current_date
	join daily_mau c on a.current_date = c.current_date
;

/************************************
DAU와 MAU의 비율. 고착도(stickiness)  월간 사용자들중 얼마나 많은 사용자가 주기적으로 방문하는가? 재방문 지표로 서비스의 활성화 지표 제공.
*************************************/
--DAU와 MAU의 비율 
with 
temp_dau as (
select :current_date as curr_date, count(distinct user_id) as dau
from ga.ga_sess
where visit_stime >= (:current_date - interval '1 days') and visit_stime < :current_date
), 
temp_mau as (
select :current_date as curr_date, count(distinct user_id) as mau
from ga.ga_sess
where visit_stime >= (:current_date - interval '30 days') and visit_stime < :current_date
)
select a.current_day, a.dau, b.mau, round(100.0 * a.dau/b.mau, 2) as stickieness
from temp_dau a
	join temp_mau b on a.curr_date = b.curr_date
;

-- 일주일간 stickiess, 평균 stickness
select *, round(100.0 * dau/mau, 2) as stickieness
	, round(avg(100.0 * dau/mau) over(), 2) as avg_stickieness
from ga.daily_acquisitions
where curr_date between to_date('2016-10-25', 'yyyy-mm-dd') and to_date('2016-10-31', 'yyyy-mm-dd')



/************************************
사용자별 월별 세션 접속 횟수 구간별 분포 집계
step 1: 사용자별 월별 접속 횟수, (월말 3일 이전 생성된 사용자 제외) 
step 2: 사용자별 월별 접속 횟수 구간별 분포 . 월별 + 접속 횟수 구간별로 Group by
step 3: gubun 별로 pivot 하여 추출
*************************************/
 
-- user 생성일자가 해당 월의 마지막 일에서 3일전인 user 추출. 
-- 월의 마지막 일자 구하기. 
-- postgresql은 last_day()함수가 없음. 때문에 해당 일자가 속한 달의 첫번째 날짜 가령 10월 5일이면 10월 1일에 1달을 더하고 거기에 1일을 뺌
-- 즉 10월 5일 -> 10월 1일 -> 11월 1일 -> 10월 31일 순으로 계산함.

select user_id, create_time, (date_trunc('month', create_time) + interval '1 month' - interval '1 day')::date
from ga.ga_users
where create_time <= (date_trunc('month', create_time) + interval '1 month' - interval '1 day')::date - 2;

-- 사용자별 월별 세션접속 횟수, 월말 3일 이전 생성된 사용자 제외 
select a.user_id, date_trunc('month', visit_stime)::date as month
	-- 사용자별 접속 건수. 고유 접속 건수가 아니므로 count(distinct user_id)를 적용하지 않음. 
	, count(*) as monthly_user_cnt  
from ga_sess a 
	join ga_users b on a.user_id = b.user_id 
where b.create_time <= (date_trunc('month', b.create_time) + interval '1 month' - interval '1 day')::date - 2
group by a.user_id, date_trunc('month', visit_stime)::date;

-- 사용자별 월별 세션 접속 횟수 구간별 집계, 월말 3일 이전 생성된 사용자 제외 
with temp_01 as (
	select a.user_id, date_trunc('month', visit_stime)::date as month, count(*) as monthly_user_cnt  
	from ga.ga_sess a 
		join ga_users b on a.user_id = b.user_id 
	where b.create_time <= (date_trunc('month', b.create_time) + interval '1 month' - interval '1 day')::date - 2
	group by a.user_id, date_trunc('month', visit_stime)::date 
)
select month
	,case when monthly_user_cnt = 1 then '0_only_first_session'
		  when monthly_user_cnt between 2 and 3 then '2_between_3'
		  when monthly_user_cnt between 4 and 8 then '4_between_8'
		  when monthly_user_cnt between 9 and 14 then '9_between_14'
		  when monthly_user_cnt between 15 and 25 then '15_between_25'
		  when monthly_user_cnt >= 26 then 'over_26' end as gubun
	, count(*) as user_cnt 
from temp_01 
group by month, 
		 case when monthly_user_cnt = 1 then '0_only_first_session'
		  when monthly_user_cnt between 2 and 3 then '2_between_3'
		  when monthly_user_cnt between 4 and 8 then '4_between_8'
		  when monthly_user_cnt between 9 and 14 then '9_between_14'
		  when monthly_user_cnt between 15 and 25 then '15_between_25'
		  when monthly_user_cnt >= 26 then 'over_26' end
order by 1, 2;

-- gubun 별로 pivot 하여 추출 
with temp_01 as (
	select a.user_id, date_trunc('month', visit_stime)::date as month, count(*) as monthly_user_cnt  
	from ga.ga_sess a 
		join ga.ga_users b 
		on a.user_id = b.user_id 
	where b.create_time <= (date_trunc('month', b.create_time) + interval '1 month' - interval '1 day')::date - 2
	group by a.user_id, date_trunc('month', visit_stime)::date 
), 
temp_02 as ( 
	select month
		,case when monthly_user_cnt = 1 then '0_only_first_session'
		      when monthly_user_cnt between 2 and 3 then '2_between_3'
		      when monthly_user_cnt between 4 and 8 then '4_between_8'
		      when monthly_user_cnt between 9 and 14 then '9_between_14'
		      when monthly_user_cnt between 15 and 25 then '15_between_25'
		      when monthly_user_cnt >= 26 then 'over_26' end as gubun
		, count(*) as user_cnt 
	from temp_01 
	group by month, 
			 case when monthly_user_cnt = 1 then '0_only_first_session'
			      when monthly_user_cnt between 2 and 3 then '2_between_3'
			      when monthly_user_cnt between 4 and 8 then '4_between_8'
			      when monthly_user_cnt between 9 and 14 then '9_between_14'
			      when monthly_user_cnt between 15 and 25 then '15_between_25'
			      when monthly_user_cnt >= 26 then 'over_26' end
)
select month, 
	sum(case when gubun='0_only_first_session' then user_cnt else 0 end) as "0_only_first_session"
	,sum(case when gubun='2_between_3' then user_cnt else 0 end) as "2_between_3"
	,sum(case when gubun='4_between_8' then user_cnt else 0 end) as "4_between_8"
	,sum(case when gubun='9_between_14' then user_cnt else 0 end) as "9_between_14"
	,sum(case when gubun='15_between_25' then user_cnt else 0 end) as "15_between_25"
	,sum(case when gubun='over_26' then user_cnt else 0 end) as "over_26"
from temp_02 
group by month order by 1;

/************************************
한달 기간중에 주간 방문 횟수별 사용자 건수
*************************************/

with
temp_01 as (
select user_id, 
	case when visit_date between '20160801' and '20160807' then '1st'
		 when visit_date between '20160808' and '20160814' then '2nd'	
		 when visit_date between '20160815' and '20160821' then '3rd'
		 when visit_date between '20160822' and '20160828' then '4th'
		 when visit_date between '20160829' and '20160904' then '5th' end as week_gubun
	, count(distinct visit_date) as daily_visit_cnt
from ga_sess
where visit_date between '20160801' and '20160831'
group by user_id
, case when visit_date between '20160801' and '20160807' then '1st'
		 when visit_date between '20160808' and '20160814' then '2nd'	
		 when visit_date between '20160815' and '20160821' then '3rd'
		 when visit_date between '20160822' and '20160828' then '4th'
		 when visit_date between '20160829' and '20160904' then '5th' end
)
select daily_visit_cnt
	, sum(case when week_gubun='1st' then 1 else 0 end) as week_1st_user_cnt
	, sum(case when week_gubun='2nd' then 1 else 0 end) as week_2nd_user_cnt
	, sum(case when week_gubun='3rd' then 1 else 0 end) as week_3rd_user_cnt
	, sum(case when week_gubun='4th' then 1 else 0 end) as week_4th_user_cnt
	, sum(case when week_gubun='5th' then 1 else 0 end) as week_5th_user_cnt
from temp_01 group by daily_visit_cnt
order by 1;

-- 임시 테이블을 이용하여 동적으로 주간 기간 설정 - 한달 기간중에 주간 방문 횟수별 사용자 건수
with 
temp_00(week_gubun, start_date, end_date) as
(
values
('1st', '20160801', '20160807')
,('2nd', '20160808', '20160814')
,('3rd', '20160815', '20160821')
,('4th', '20160822', '20160828')
,('5th', '20160829', '20160904')
), 
temp_01 as 
(
select  a.user_id, b.week_gubun
	, count(distinct visit_date) as daily_visit_cnt
from ga_sess a
	join temp_00 b on a.visit_date between b.start_date and end_date
 --where a.visit_date between (select min(start_date) from temp_00) and (select max(end_date) from temp_00) -- 성능을 위해서
group by a.user_id, b.week_gubun
)
select daily_visit_cnt
	, sum(case when week_gubun='1st' then 1 else 0 end) as week_1st_user_cnt
	, sum(case when week_gubun='2nd' then 1 else 0 end) as week_2nd_user_cnt
	, sum(case when week_gubun='3rd' then 1 else 0 end) as week_3rd_user_cnt
	, sum(case when week_gubun='4th' then 1 else 0 end) as week_4th_user_cnt
	, sum(case when week_gubun='5th' then 1 else 0 end) as week_5th_user_cnt
from temp_01 group by daily_visit_cnt
order by 1;


/************************************
사용자가 첫 세션 접속 후 두번째 세션 접속까지 걸리는 평균, 최대, 최소, 4분위 percentile 시간 추출 
step 1: 사용자 별로 접속 시간에 따라 session 별 순서 매김. 
step 2: session 별 순서가 첫번째와 두번째 인것 추출
step 3: 사용자 별로 첫번째 세션의 접속 이후 두번째 세션의 접속 시간 차이를 가져 오기
step 4: step 3의 데이터를 전체 평균, 최대, 최소, 4분위 percentile 시간 구하기 
*************************************/

-- 사용자 별로 접속 시간에 따라 session 별 순서 매김.
select user_id, row_number() over (partition by user_id order by visit_stime) as session_rnum 
	, visit_stime
	-- 추후에 1개 session만 있는 사용자는 제외하기 위해 사용. 
	, count(*) over (partition by user_id) as session_cnt
from ga_sess order by user_id, session_rnum;

--session 별 순서가 첫번째와 두번째 인것 추출하고 사용자 별로 첫번째 세션의 접속 이후 두번째 세션의 접속 시간 차이를 가져 오기
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) as session_rnum 
	, visit_stime
	-- 추후에 1개 session만 있는 사용자는 제외하기 위해 사용. 
	, count(*) over (partition by user_id) as session_cnt
from ga_sess
)
select user_id
	-- 사용자별로 첫번째 세션, 두번째 세션만 있으므로 max(visit_stime)이 두번째 세션 접속 시간, min(visit_stime)이 첫번째 세션 접속 시간.
	, max(visit_stime) - min(visit_stime) as sess_time_diff
from temp_01 where session_rnum <= 2 and session_cnt > 1 -- 첫번째 두번째 세션만 가져오되 첫번째 접속만 있는 사용자를 제외하기 
group by user_id;

-- step 3의 데이터를 전체 평균, 최대값, 최소값, 4분위 percentile  구하기. 
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) as session_rnum 
		, visit_stime
		-- 추후에 1개 session만 있는 사용자는 제외하기 위해 사용. 
		, count(*) over (partition by user_id) as session_cnt
	from ga_sess
), 
temp_02 as (
	select user_id
		-- 사용자별로 첫번째 세션, 두번째 세션만 있으므로 max(visit_stime)이 두번째 세션 접속 시간, min(visit_stime)이 첫번째 세션 접속 시간.	
		, max(visit_stime) - min(visit_stime) as sess_time_diff
	from temp_01 where session_rnum <= 2 and session_cnt > 1
	group by user_id
)
-- postgresql avg(time)은 interval이 제대로 고려되지 않음. justify_inteval()을 적용해야 함. 
select justify_interval(avg(sess_time_diff)) as avg_time
    , max(sess_time_diff) as max_time, min(sess_time_diff) as min_time 
	, percentile_disc(0.25) within group (order by sess_time_diff) as percentile_1
	, percentile_disc(0.5) within group (order by sess_time_diff)	as percentile_2
	, percentile_disc(0.75) within group (order by sess_time_diff)	as percentile_3
	, percentile_disc(1.0) within group (order by sess_time_diff)	as percentile_4
from temp_02
where sess_time_diff::interval > interval '0 second';


/************************************
MAU를 신규 사용자, 기존 사용자(재 방문) 건수로 분리하여 추출(세션 건수도 함께 추출)
*************************************/

with
temp_01 as (
select a.sess_id, a.user_id, a.visit_stime, b.create_time
	, case when b.create_time >= (:current_date - interval '30 days') and b.create_time < :current_date then 1
	     else 0 end as is_new_user
from ga.ga_sess a
	join ga.ga_users b on a.user_id = b.user_id
where visit_stime >= (:current_date - interval '30 days') and visit_stime < :current_date
)
select count(distinct user_id) as user_cnt
	, count(distinct case when is_new_user = 1 then user_id end) as new_user_cnt
	, count(distinct case when is_new_user = 0 then user_id end) as repeat_user_cnt
	, count(*) as sess_cnt
from temp_01;



/************************************
채널별로 MAU를 신규 사용자, 기존 사용자로 나누고, 채널별 비율까지 함께 계산. 
*************************************/
select channel_grouping, count(distinct user_id) from ga.ga_sess group by channel_grouping;

with
temp_01 as (
select a.sess_id, a.user_id, a.visit_stime, b.create_time, channel_grouping
	, case when b.create_time >= (:current_date - interval '30 days') and b.create_time < :current_date then 1
	     else 0 end as is_new_user
from ga.ga_sess a
	join ga.ga_users b on a.user_id = b.user_id
where visit_stime >= (:current_date - interval '30 days') and visit_stime < :current_date
),
temp_02 as (
select channel_grouping
	, count(distinct case when is_new_user = 1 then user_id end) as new_user_cnt
	, count(distinct case when is_new_user = 0 then user_id end) as repeat_user_cnt
	, count(distinct user_id) as channel_user_cnt
	, count(*) as sess_cnt
from temp_01
group by channel_grouping
)
select channel_grouping, new_user_cnt, repeat_user_cnt, channel_user_cnt, sess_cnt
	, 100.0*new_user_cnt/sum(new_user_cnt) over () as new_user_cnt_by_channel
	, 100.0*repeat_user_cnt/sum(repeat_user_cnt) over () as repeat_user_cnt_by_channel
from temp_02;


/************************************
채널별 고유 사용자 건수와 매출금액 및 비율, 주문 사용자 건수와 주문 매출 금액 및 비율
채널별로 고유 사용자 건수와 매출 금액을 구하고 고유 사용자 건수 대비 매출 금액 비율을 추출. 
또한 고유 사용자 중에서 주문을 수행한 사용자 건수를 추출 후 주문 사용자 건수 대비 매출 금액 비율을 추출
*************************************/
with temp_01 as (
	select a.sess_id, a.user_id, a.channel_grouping
		, b.order_id, b.order_time, c.product_id, c.prod_revenue 
	from ga_sess a
		left join orders b on a.sess_id = b.sess_id
		left join order_items c on b.order_id = c.order_id
	where a.visit_stime >= (:current_date - interval '30 days') and a.visit_stime < :current_date
)
select channel_grouping
	, sum(prod_revenue) as ch_amt -- 채널별 매출
	--, count(distinct sess_id) as ch_sess_cnt -- 채널별 고유 세션 수
	, count(distinct user_id) as ch_user_cnt -- 채널별 고유 사용자 수
	--, count(distinct case when order_id is not null then sess_id end) as ch_ord_sess_cnt -- 채널별 주문 고유 세션수
	, count(distinct case when order_id is not null then user_id end) as ch_ord_user_cnt -- 채널별 주문 고유 사용자수
	--, sum(prod_revenue)/count(distinct sess_id) as ch_amt_per_sess -- 접속 세션별 주문 매출 금액
	, sum(prod_revenue)/count(distinct user_id) as ch_amt_per_user -- 접속 고유 사용자별 주문 매출 금액
	-- 주문 세션별 매출 금액
	--, sum(prod_revenue)/count(distinct case when order_id is not null then sess_id end) as ch_ord_amt_per_sess
	-- 주문 고유 사용자별 매출 금액
	, sum(prod_revenue)/count(distinct case when order_id is not null then user_id end) as ch_ord_amt_per_user
from temp_01
group by channel_grouping order by ch_user_cnt desc;


/************************************
device 별 접속 건수 , 전체 건수대비 device별 접속 건수
일/주별 device별 접속건수
*************************************/

-- device 별 접속 건수 
select device_category, count(*) as device_cnt
from ga_sess group by device_category;

-- 전체 건수 대비 device별 접속 건수
with temp_01 as (
select count(*) as total_cnt from ga_sess 
),
temp_02 as (
select device_category, count(*) as device_cnt
from ga_sess group by device_category 
)
select device_category, device_cnt, 1.0*device_cnt/total_cnt
from temp_01, temp_02;

-- mobile과 tablet을 함께 합쳐서 mobile_tablet으로 접속 건수 조사
select 
	case when device_category in ('mobile', 'tablet') then 'mobile_tablet'
			  when device_category = 'desktop' then 'desktop' end as device_category
	, count(*) as device_cnt
from ga_sess
group by case when device_category in ('mobile', 'tablet') then 'mobile_tablet'
			  when device_category = 'desktop' then 'desktop' end;


-- 일별 접속자를 desktop, mobile, tablet 에 따라 접속자수 계산. 
select date_trunc('day', visit_stime)
	, sum(case when device_category = 'desktop' then 1 else 0 end) as desktop_cnt
	, sum(case when device_category = 'mobile' then 1 else 0 end) as mobile_cnt
	, sum(case when device_category = 'tablet' then 1 else 0 end) as tablet_cnt
	, count(*)
from ga_sess 
group by date_trunc('day', visit_stime);

-- 주별 접속자를 desktop, mobile, tablet 에 따라 접속자수 계산.
select date_trunc('week', visit_stime)
	, sum(case when device_category = 'desktop' then 1 else 0 end) as desktop_cnt
	, sum(case when device_category = 'mobile' then 1 else 0 end) as mobile_cnt
	, sum(case when device_category = 'tablet' then 1 else 0 end) as tablet_cnt
	, count(*)
from ga_sess 
group by date_trunc('week', visit_stime);


-- 접속 device 별 매출과 device별 세션당 매출과 사용자별 매출액 추출. 
with temp_01 as (
	select a.order_id, a.order_time,  b.product_id, b.prod_revenue, c.sess_id, c.user_id, c.device_category 
	from orders a
		join order_items b 
			on a.order_id = b.order_id
		join ga_sess c
			on a.sess_id = c.sess_id 
	where a.order_status = 'delivered'
)
select device_category, sum(prod_revenue) device_sum_amount
	, count(distinct sess_id) as sess_cnt
	, count(distinct user_id) as user_cnt
	, sum(prod_revenue)/count(distinct sess_id) as sum_amount_per_sess
	, sum(prod_revenue)/count(distinct user_id) as sum_amount_per_user
from temp_01;
group by device_category;