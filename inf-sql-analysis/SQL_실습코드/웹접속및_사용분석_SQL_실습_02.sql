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
DAU, WAU, MAU 및 전주 대비 WAU 비율
*************************************/
-- 일별 방문한 고객 수(DAU)
select date_trunc('day', visit_stime)::date as d_day, count(distinct user_id) as user_cnt 
from ga.ga_sess 
group by date_trunc('day', visit_stime)::date;

-- 주별 방문한 고객수(WAU)
select date_trunc('week', visit_stime)::date as week_day, count(distinct user_id) as user_cnt
from ga.ga_sess 
group by date_trunc('week', visit_stime)::date order by 1;

-- 월별 방문한 고객수(MAU)
select date_trunc('month', visit_stime)::date as month_day, count(distinct user_id) as user_cnt 
from ga.ga_sess 
group by date_trunc('month', visit_stime)::date;

-- 전주 대비 WAU 비율
with temp_01 as (
	select date_trunc('week', visit_stime)::date as week_day
		, count(distinct user_id) as user_cnt
	from ga.ga_sess group by date_trunc('week', visit_stime)::date
)
select week_day, user_cnt
	-- 만일 이전 데이터가 없으면 현재 user_cnt를 가져옴. 
	, coalesce(lag(user_cnt) over (order by week_day), user_cnt) as prev_user_cnt
	-- 만일 이전 데이터가 없으면 100
	, coalesce(round(100.0 * user_cnt/lag(user_cnt) over (order by week_day), 2), 100.0) as prev_pct
from temp_01;

/************************************
DAU와 MAU의 비율. stickiness 월간 사용자들중 얼마나 어제 재 접속했는가? 재방문 지표로 서비스의 활성화 지표 제공.  
*************************************/
 
with temp_dau as (
select to_char(date_trunc('day', visit_stime)::date, 'yyyymmdd') as d_day
	, count(distinct user_id) as dau
from ga.ga_sess 
where visit_stime between to_date('2016-08-01', 'yyyy-mm-dd') and to_date('2016-08-31', 'yyyy-mm-dd')
group by date_trunc('day', visit_stime)::date
), 
temp_mau as ( 
select to_char(date_trunc('month', visit_stime)::date, 'yyyymm') as month_day
	, count(distinct user_id) as mau 
from ga.ga_sess 
where visit_stime between to_char('2016-08-01', 'yyyy-mm-dd') and to_char('2016-08-31', 'yyyy-mm-dd')
group by date_trunc('month', visit_stime)::date
)
select a.d_day, a.dau, b.month_day, b.mau
	, round(100.0 * a.dau/b.mau, 2) as dau_mau_ratio
from temp_dau a join temp_mau b
on substring(a.d_day, 1, 6) = b.month_day

-- 전체 기간중 고객 별로 가장 많이 방문한 순으로 조회. 
select user_id, count(*)
from ga_sess group by user_id order by 2 desc;

-- 특정 횟수 구간별로 방문한 고객 수 - 8월 한달간 1회, 2-3회, 4-8, 9-14, 15-25, 26회 이상 방문한 고객 건수 조회.
with temp_01 as (  
	select user_id, count(*) as cnt
	from ga_sess where visit_stime between to_date('2016-08-01', 'yyyy-mm-dd') and to_date('2016-08-31 23:59:59', 'yyyy-mm-dd hh24:mi:ss') group by user_id 
)
select case when cnt = 1 then '0_only_first_session'
			  when cnt between 2 and 3 then '1_lt_3'
			  when cnt between 4 and 8 then '2_lt_8'
			  when cnt between 9 and 14 then '3_lt_14'
			  when cnt between 15 and 25 then '4_lt_25'
			  when cnt >= 26 then '5_over_26' end as gubun
	   , count(user_id) as user_cnt
from temp_01 
group by case when cnt = 1 then '0_only_first_session'
			  when cnt between 2 and 3 then '1_lt_3'
			  when cnt between 4 and 8 then '2_lt_8'
			  when cnt between 9 and 14 then '3_lt_14'
			  when cnt between 15 and 25 then '4_lt_25'
			  when cnt >= 26 then '5_over_26' end
order by 1;

-- 사용자는 최소 8월말 기준으로 3일 전에 생성되어야 함. 8월 한달간 1회, 2-3회, 4-8, 9-14, 15-25, 26회 이상 방문한 고객 건수 조회
with temp_01 as (  
	select a.user_id, count(*) as cnt
	from ga_sess a 
		join ga_users b 
		on a.user_id = b.user_id 
	where visit_stime between to_date('2016-08-01', 'yyyy-mm-dd') and to_date('2016-08-31 23:59:59', 'yyyy-mm-dd hh24:mi:ss') 
	and b.create_time <= to_date('2016-08-31', 'yyyy-mm-dd hh24:mi:ss') - 2
	group by a.user_id 
)
select case when cnt = 1 then '0_only_first_session'
			  when cnt between 2 and 3 then '1_lt_3'
			  when cnt between 4 and 8 then '2_lt_8'
			  when cnt between 9 and 14 then '3_lt_14'
			  when cnt between 15 and 25 then '4_lt_25'
			  when cnt >= 26 then '5_over_26' end as gubun
	   , count(user_id) as user_cnt
from temp_01 
group by case when cnt = 1 then '0_only_first_session'
			  when cnt between 2 and 3 then '1_lt_3'
			  when cnt between 4 and 8 then '2_lt_8'
			  when cnt between 9 and 14 then '3_lt_14'
			  when cnt between 15 and 25 then '4_lt_25'
			  when cnt >= 26 then '5_over_26' end
order by 1;


/* 월별 특정 횟수 구간별로 방문한 고객 수 구하기 
  아래 스텝별로 생성. 
1. 사용자별 월별 접속 횟수, 월말 3일 이전 생성된 사용자 제외 
2.  사용자별 월별 접속 구간별 횟수, 월말 3일 이전 생성된 사용자 제외
3.  gubun 별로 pivot 하여 추출
*/

-- user 생성일자가 해당 월의 마지막 일에서 3일전인 user 추출. 
select user_id, create_time, (date_trunc('month', create_time) + interval '1 month' - interval '1 day')::date
from ga_users
where create_time <= (date_trunc('month', create_time) + interval '1 month' - interval '1 day')::date - 2;

-- 사용자별 월별 접속 횟수, 월말 3일 이전 생성된 사용자 제외 
select a.user_id, date_trunc('month', visit_stime)::date as month, count(*) as monthly_user_cnt  
from ga_sess a 
	join ga_users b 
	on a.user_id = b.user_id 
where b.create_time <= (date_trunc('month', b.create_time) + interval '1 month' - interval '1 day')::date - 2
group by a.user_id, date_trunc('month', visit_stime)::date;

-- 사용자별 월별 접속 구간별 횟수, 월말 3일 이전 생성된 사용자 제외 
with temp_01 as (
	select a.user_id, date_trunc('month', visit_stime)::date as month, count(*) as monthly_user_cnt  
	from ga_sess a 
		join ga_users b 
		on a.user_id = b.user_id 
	where b.create_time <= (date_trunc('month', b.create_time) + interval '1 month' - interval '1 day')::date - 2
	group by a.user_id, date_trunc('month', visit_stime)::date 
)
select month
	,case when monthly_user_cnt = 1 then '0_only_first_session'
		  when monthly_user_cnt between 2 and 3 then '1_lt_3'
		  when monthly_user_cnt between 4 and 8 then '2_lt_8'
		  when monthly_user_cnt between 9 and 14 then '3_lt_14'
		  when monthly_user_cnt between 15 and 25 then '4_lt_25'
		  when monthly_user_cnt >= 26 then '5_over_26' end as gubun
	, count(*) as user_cnt 
from temp_01 
group by month, 
		 case when monthly_user_cnt = 1 then '0_only_first_session'
			  when monthly_user_cnt between 2 and 3 then '1_lt_3'
			  when monthly_user_cnt between 4 and 8 then '2_lt_8'
			  when monthly_user_cnt between 9 and 14 then '3_lt_14'
			  when monthly_user_cnt between 15 and 25 then '4_lt_25'
			  when monthly_user_cnt >= 26 then '5_over_26' end
order by 1, 2;

-- gubun 별로 pivot 하여 추출 
with temp_01 as (
	select a.user_id, date_trunc('month', visit_stime)::date as month, count(*) as monthly_user_cnt  
	from ga_sess a 
		join ga_users b 
		on a.user_id = b.user_id 
	where b.create_time <= (date_trunc('month', b.create_time) + interval '1 month' - interval '1 day')::date - 2
	group by a.user_id, date_trunc('month', visit_stime)::date 
), 
temp_02 as ( 
	select month
		,case when monthly_user_cnt = 1 then '0_only_first_session'
			  when monthly_user_cnt between 2 and 3 then '1_lt_3'
			  when monthly_user_cnt between 4 and 8 then '2_lt_8'
			  when monthly_user_cnt between 9 and 14 then '3_lt_14'
			  when monthly_user_cnt between 15 and 25 then '4_lt_25'
			  when monthly_user_cnt >= 26 then '5_over_26' end as gubun
		, count(*) as user_cnt 
	from temp_01 
	group by month, 
			 case when monthly_user_cnt = 1 then '0_only_first_session'
				  when monthly_user_cnt between 2 and 3 then '1_lt_3'
				  when monthly_user_cnt between 4 and 8 then '2_lt_8'
				  when monthly_user_cnt between 9 and 14 then '3_lt_14'
				  when monthly_user_cnt between 15 and 25 then '4_lt_25'
				  when monthly_user_cnt >= 26 then '5_over_26' end
)
select month, 
	sum(case when gubun='0_only_first_session' then user_cnt else 0 end) as "0_only_first_session"
	,sum(case when gubun='1_lt_3' then user_cnt else 0 end) as "1_lt_3"
	,sum(case when gubun='2_lt_8' then user_cnt else 0 end) as "2_lt_8"
	,sum(case when gubun='3_lt_14' then user_cnt else 0 end) as "3_lt_14"
	,sum(case when gubun='4_lt_25' then user_cnt else 0 end) as "4_lt_25"
	,sum(case when gubun='5_over_26' then user_cnt else 0 end) as "5_over_26"
from temp_02 
group by month order by 1;

/* 사용자가 첫 접속 후 두번째 접속까지 걸리는 평균, 최대 시간 추출 
step 1: 사용자 별로 접속 시간에 따라 session 별 순서 매김. 
step 2: session 별 순서가 첫번째와 두번째 인것 추출
step 3: 사용자 별로 첫번째 세션의 접속 이후 두번째 세션의 접속 시간 차이를 가져 오기
step 4: step 3의 데이터를 전체 평균/최대값 구하기 
*/
-- 사용자 별로 접속 시간에 따라 session 별 순서 매김.
select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
	, visit_stime
from ga_sess order by user_id;

--session 별 순서가 첫번째와 두번째 인것 추출하고 사용자 별로 첫번째 세션의 접속 이후 두번째 세션의 접속 시간 차이를 가져 오기
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq
		--, count(*) over (partition by user_id) --session_cnt 만약 첫번째 접속만 있는 사용자를 제외하기를 원한다면 
		, visit_stime
	from ga_sess
)
select user_id, max(visit_stime) - min(visit_stime) as sess_time_diff
from temp_01 where session_seq <= 2 --and session_cnt > 1 --session_cnt 만약 첫번째 접속만 있는 사용자를 제외하기를 원한다면 
group by user_id;

-- step 3의 데이터를 전체 평균/최대값 구하기. 이때 사용자가 최초 접속만 있는 경우는 제외. 
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
		, visit_stime
	from ga_sess
), 
temp_02 as (
	select user_id, max(visit_stime) - min(visit_stime) as sess_time_diff
	from temp_01 where session_seq <= 2 
	group by user_id
)
select avg(sess_time_diff), max(sess_time_diff), min(sess_time_diff) 
from temp_02
where sess_time_diff::interval > interval '0 second';

/* 아래와 같이 lead()를 이용하여 구할 수도 있음. 결과가 앞에와 다름. 이유 파악 필요 */
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
		, visit_stime
	from ga_sess
), 
temp_02 as (
	select  user_id, session_seq, visit_stime 
		, lead(visit_stime, 1) over(partition by user_id order by visit_stime) as visit_stime_2nd
		from temp_01
)
select avg(time_diff_1to2)
from (
	select user_id, session_seq, visit_stime
		, visit_stime_2nd - visit_stime as time_diff_1to2
	from temp_02 where session_seq = 1 -- session_seq가 1인 데이터에 time_diff_xxx가 1->2, 2->3, 3-> 4 세션간의 시간차이가 들어가 있음. 
) a;

-- 첫 접속 후 두번째 접속까지 걸리는 시간을 4분위로 표시
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
		, visit_stime
	from ga_sess
), 
temp_02 as (
	select user_id, max(visit_stime) - min(visit_stime) as sess_time_diff
	from temp_01 where session_seq <= 2 
	group by user_id
)
select percentile_disc(0.25) within group (order by sess_time_diff) as quantile_1
	, percentile_disc(0.5) within group (order by sess_time_diff)	as quantile_2
	, percentile_disc(0.75) within group (order by sess_time_diff)	as quantile_3
	, percentile_disc(1.0) within group (order by sess_time_diff)	as quantile_4
from temp_02
where sess_time_diff::interval > interval '0 second';


/* 사용자가 첫번째 세션접속에서 두번째 세션 접속까지 걸리는 평균 시간외에, 두번째->세번째, 세번째->네번째 까지 걸리는 평균 시간 계산
   아래 sql에서 1->2 까지의 평균 접속 시간이 이전에 구한 SQL과 시간이 달라짐.확인 요망. 
   step 1: 사용자별로 lead()를 이용하여 다음 접속, 다다음 접속, 다다다음 접속 시간을 가져옴. 
 */
with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
		, visit_stime
	from ga_sess
)
select  user_id, session_seq, visit_stime 
	, lead(visit_stime, 1) over(partition by user_id order by visit_stime) as visit_stime_2nd
	, lead(visit_stime, 2) over(partition by user_id order by visit_stime) as visit_stime_3rd
	, lead(visit_stime, 3) over(partition by user_id order by visit_stime) as visit_stime_4th
from temp_01 order by user_id, session_seq;

with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
		, visit_stime
	from ga_sess
), 
temp_02 as (
	select  user_id, session_seq, visit_stime 
		, lead(visit_stime, 1) over(partition by user_id order by visit_stime) as visit_stime_2nd
		, lead(visit_stime, 2) over(partition by user_id order by visit_stime) as visit_stime_3rd
		, lead(visit_stime, 3) over(partition by user_id order by visit_stime) as visit_stime_4th
	from temp_01
)
select avg(time_diff_1to2), avg(time_diff_2to3), avg(time_diff_3to4)
from (
	select user_id, session_seq, visit_stime
		, visit_stime_2nd - visit_stime as time_diff_1to2
		, visit_stime_3rd - visit_stime_2nd as time_diff_2to3
		, visit_stime_4th - visit_stime_3rd as time_diff_3to4
	from temp_02 where session_seq = 1 -- session_seq가 1인 데이터에 time_diff_xxx가 1->2, 2->3, 3-> 4 세션간의 시간차이가 들어가 있음. 
) a;

with
temp_01 as (
	select user_id, row_number() over (partition by user_id order by visit_stime) session_seq 
		, visit_stime
	from ga_sess
), 
temp_02 as (
	select  user_id, session_seq, visit_stime 
		, lead(visit_stime, 1) over(partition by user_id order by visit_stime) as visit_stime_2nd
		, lead(visit_stime, 2) over(partition by user_id order by visit_stime) as visit_stime_3rd
		, lead(visit_stime, 3) over(partition by user_id order by visit_stime) as visit_stime_4th
	from temp_01
)
select user_id, session_seq, visit_stime
	, visit_stime_2nd - visit_stime as time_diff_1to2
	, visit_stime_3rd - visit_stime_2nd as time_diff_2to3
	, visit_stime_4th - visit_stime_3rd as time_diff_3to4
from temp_02;

/* device별 접속 건수 */

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

-- 사용자 생성 날짜 별 일주일간 잔존(Retention) 접속 횟수
with temp_01 as (
	select a.user_id, date_trunc('day', a.create_time) as user_create_date,  date_trunc('day', b.visit_stime) as sess_visit_date
		, count(*) cnt
	from ga_users a
		join ga_sess b
			on a.user_id = b.user_id
	group by a.user_id, date_trunc('day', a.create_time), date_trunc('day', b.visit_stime)
)
select user_create_date, count(*) as daily_create_visit_cnt
	, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else 0 end ) as d1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else 0 end) as d2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else 0 end) as d3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else 0 end) as d4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else 0 end) as d5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else 0 end) as d6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else 0 end) as d7_cnt
from temp_01 
group by user_create_date order by 1;

-- 사용자 생성 날짜 및 Device별 일주일간 잔존(Retention) 접속 횟수
with temp_01 as (
	select a.user_id, device_category
		, date_trunc('day', a.create_time) as user_create_date,  date_trunc('day', b.visit_stime) as sess_visit_date
		, count(*) cnt
	from ga_users a
		join ga_sess b
			on a.user_id = b.user_id
	group by a.user_id, device_category, date_trunc('day', a.create_time), date_trunc('day', b.visit_stime)
)
select user_create_date, device_category, count(*) as daily_create_visit_cnt
	, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else 0 end ) as d1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else 0 end) as d2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else 0 end) as d3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else 0 end) as d4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else 0 end) as d5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else 0 end) as d6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else 0 end) as d7_cnt
from temp_01 
group by user_create_date, device_category order by 1, 2;

--사용자 생성 날짜 별 일주일간 잔존율(Retention ratio)
with temp_01 as (
	select a.user_id, date_trunc('day', a.create_time) as user_create_date,  date_trunc('day', b.visit_stime) as sess_visit_date
		, count(*) cnt
	from ga_users a
		join ga_sess b
			on a.user_id = b.user_id
	group by a.user_id, date_trunc('day', a.create_time), date_trunc('day', b.visit_stime)
)
select user_create_date, count(*) as daily_create_visit_cnt
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else 0 end)/count(*) as d1_retention_ratio
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else 0 end)/count(*) as d2_retention_ratio
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else 0 end)/count(*) as d3_retention_ratio
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else 0 end)/count(*) as d4_retention_ratio
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else 0 end)/count(*) as d5_retention_ratio
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else 0 end)/count(*) as d6_retention_ratio
	, 1.0*sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else 0 end)/count(*) as d7_retention_ratio
from temp_01 
group by user_create_date order by 1;

--사용자 생성 날짜 및 Device별 일주일간 잔존율(Retention ratio)
with temp_01 as (
	select a.user_id, device_category, date_trunc('day', a.create_time) as user_create_date,  date_trunc('day', b.visit_stime) as sess_visit_date
		, count(*) cnt
	from ga_users a
		join ga_sess b
			on a.user_id = b.user_id
	group by a.user_id, device_category, date_trunc('day', a.create_time), date_trunc('day', b.visit_stime)
)
select user_create_date, device_category, count(*) as daily_create_visit_cnt
	, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else 0 end)/count(*) as d1_retention_ratio
	, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else 0 end)/count(*) as d2_retention_ratio
	, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else 0 end)/count(*) as d3_retention_ratio
	, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else 0 end)/count(*) as d4_retention_ratio
	, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else 0 end)/count(*) as d5_retention_ratio
	, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else 0 end)/count(*) as d6_retention_ratio
	, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else 0 end)/count(*) as d7_retention_ratio
from temp_01 
group by user_create_date, device_category order by 1, 2;

-- 잔존 고객수를 사선으로 표시하기. 
with temp_01 as (
	select a.user_id, date_trunc('day', a.create_time) as user_create_date,  date_trunc('day', b.visit_stime) as sess_visit_date
		, count(*) cnt
	from ga_users a
		join ga_sess b
			on a.user_id = b.user_id
	group by a.user_id, date_trunc('day', a.create_time), date_trunc('day', b.visit_stime)
),
temp_02 as ( 
	select user_create_date, count(*) as daily_create_visit_cnt
		, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else 0 end ) as d1_cnt
		, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else 0 end) as d2_cnt
		, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else 0 end) as d3_cnt
		, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else 0 end) as d4_cnt
		, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else 0 end) as d5_cnt
		, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else 0 end) as d6_cnt
		, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else 0 end) as d7_cnt
from temp_01 group by user_create_date order by 1
)
select user_create_date, daily_create_visit_cnt,
       case when user_create_date + interval '1 day' > to_date('2016-08-05', 'yyyy-mm-dd') then null else d1_cnt end as d1_cnt,
	   case when user_create_date + interval '2 day' > to_date('2016-08-05', 'yyyy-mm-dd') then null else d2_cnt end as d2_cnt,
	   case when user_create_date + interval '3 day' > to_date('2016-08-05', 'yyyy-mm-dd') then null else d3_cnt end as d3_cnt,
	   case when user_create_date + interval '4 day' > to_date('2016-08-05', 'yyyy-mm-dd') then null else d4_cnt end as d4_cnt,
	   case when user_create_date + interval '5 day' > to_date('2016-08-05', 'yyyy-mm-dd') then null else d5_cnt end as d5_cnt
from temp_02;


-- page 별 view 건수 조회. 가장 건수가 많은 순으로 정렬
select page_path, count(*) from ga_sess_hits
group by page_path order by 2 desc;

-- sessiong level에서 landing page가 없을 경우, ga_sess_hits에서 landing page 추출하기.  
with temp_01 as ( 
	select sess_id, max(first_page) as landing_page_name
	from 
	(
	select sess_id, hit_seq, page_path
		, first_value(page_path) over (partition by sess_id order by hit_seq) first_page
	from ga_sess_hits
	) a group by sess_id
)
select landing_page_name, count(*) 
from temp_01 group by landing_page_name order by 2 desc;


-- bounced session 추출. 
select sess_id, count(*) from ga_sess_hits
group by sess_id having count(*) = 1;

-- sessiong level에서 landing page가 없을 경우, ga_sess_hits에서 landing page 추출하고, 전체 세션에서 bounced session건수와 비율 조사. 
with temp_01 as ( 
	select sess_id, max(first_page) as landing_page_name, count(*) page_cnt
	from (
		select sess_id, hit_seq, page_path
			, first_value(page_path) over (partition by sess_id order by hit_seq) first_page
		from ga_sess_hits
	) a group by sess_id
)
select sum(case when page_cnt = 1 then 1 else 0 end) as bounce_cnt, count(*) total_sess_cnt
	, 1.0*sum(case when page_cnt = 1 then 1 else 0 end)/count(*) as bounce_ratio 
from temp_01;

-- landing page 별 bounce ratio
with temp_01 as ( 
	select sess_id, max(first_page) as landing_page_name, count(*) page_cnt
	from (
		select sess_id, hit_seq, page_path
			, first_value(page_path) over (partition by sess_id order by hit_seq) first_page
		from ga_sess_hits
	) a group by sess_id
)
select landing_page_name
	, sum(case when page_cnt = 1 then 1 else 0 end) as bounce_cnt
	, count(*) total_sess_cnt
	, 1.0*sum(case when page_cnt = 1 then 1 else 0 end)/count(*) as bounce_ratio
from temp_01
group by landing_page_name order by 3 desc;


-- landing page + exit page 별 세션 건수
with temp_01 as ( 
	select sess_id, max(first_page) as landing_page_name
		, max(last_page) as exit_page_name, count(*) page_cnt
	from (
		select sess_id, hit_seq, page_path
			, first_value(page_path) over (partition by sess_id order by hit_seq) first_page
			, last_value(page_path) over (partition by sess_id order by hit_seq 
			                              rows between unbounded preceding and unbounded following) as last_page
		from ga_sess_hits
	) a group by sess_id
)
select landing_page_name, exit_page_name, count(*) 
from temp_01
group by 1, 2 order by 3 desc;


-- 일자별 landing page 의 세션 건수

with temp_01 as ( 
	select sess_id, max(first_page) as landing_page_name, count(*) page_cnt
	from (
		select sess_id, hit_seq, page_path
			, first_value(page_path) over (partition by sess_id order by hit_seq) first_page
		from ga_sess_hits
	) a group by sess_id
);

-- 일자별 세션 접속수가 가장 많은 top3 landing page와 접속 건수 추출
with temp_01 as (
	select visit_date, first_page as landing_page, count(distinct sess_id) as sess_cnt 
	from (
		select a.sess_id, date_trunc('day', b.visit_stime) as visit_date 
			, first_value(page_path) over (partition by a.sess_id order by hit_seq) first_page
		from ga_sess_hits a
			join ga_sess b 
				on a.sess_id = b.sess_id
	) a group by 1, 2
)
select visit_date, landing_page, sess_cnt 
from (
	select * 
		, row_number() over (partition by visit_date order by sess_cnt desc) rnum
	from temp_01
) a where rnum <= 3;

-- 일자별 세션 접속수가 가장 많은 top3 landing page와 접속 건수 추출을 pivot 형태로 수행. 
with temp_01 as (
	select visit_date, first_page as landing_page, count(distinct sess_id) as sess_cnt 
	from (
		select a.sess_id, date_trunc('day', b.visit_stime) as visit_date 
			, first_value(page_path) over (partition by a.sess_id order by hit_seq) first_page
		from ga_sess_hits a
			join ga_sess b 
				on a.sess_id = b.sess_id
	) a group by 1, 2
)
select visit_date, 
	max(case when rnum = 1 then landing_page end) as landing_page_1st
	-- sum()을 max()로 바꾸어도 무방
	, sum(case when rnum = 1 then sess_cnt end) as landing_page_1st_cnt
	, max(case when rnum = 2 then landing_page end) as landing_page_2nd
	, sum(case when rnum = 2 then sess_cnt end) as landing_page_2nd_cnt 
	, max(case when rnum = 3 then landing_page end) as landing_page_3rd
	, sum(case when rnum = 3 then sess_cnt end) as landing_page_3rd_cnt 
from (
	select * 
		, row_number() over (partition by visit_date order by sess_cnt desc) rnum
	from temp_01
) a where rnum <= 3
group by visit_date;



-- conversion funnel 작업 중. 
/* 
   Unknown = 0.
   Click through of product lists = 1, 
   Product detail views = 2, 
   Add product(s) to cart = 3, 
   Remove product(s) from cart = 4, 
   Check out = 5, 
   Completed purchase = 6, 
   Refund of purchase = 7, 
   Checkout options = 8
 */
select action_type, count(*) as cnt from ga_sess_hits group by action_type order by 2 desc;

select action_type, cnt
	, sum(cnt) over ()
	, first_value(cnt) over (order by action_type)
	, lag(cnt) over ( order by action_type)
from (
	select action_type, count(*) as cnt from ga_sess_hits where action_type in ('0', '1', '2', '3', '6') group by action_type
) a;


with
temp_01 as ( 
select action_type, count(*) as cnt 
from ga_sess_hits 
group by action_type
)
select action_type, cnt, 
	lag(cnt) over (order by action_type) as prev_cnt,
	1.0 * cnt/lag(cnt) over (order by action_type) as prev_ratio
from temp_01 where action_type in ('0', '1', '2', '3', '5', '6');

-- action_type별로 funnel 구함. 
/* action_type이 0 인것이 하나의 개별 세션에서 매우 많이 있음. action_type=0 을 개별 세션별로 한개만 할지 결정 필요 */
with
temp_01 as ( 
select action_type, count(*) as cnt 
from ga_sess_hits 
group by action_type
)
select action_type, cnt
	, lag(cnt) over (order by action_type) as prev_cnt
	, 1.0 * cnt/lag(cnt) over (order by action_type) as prev_ratio
	, first_value(cnt) over() as action_0_cnt
from temp_01 where action_type in ('0', '1', '2', '3', '5', '6')


-- pivot 형태로 funnel 구함. 
with
temp_01 as ( 
select action_type, count(*) as cnt 
from ga_sess_hits 
group by action_type
),
temp_02 as (
select action_type, cnt
	, lag(cnt) over (order by action_type) as prev_cnt
	, 1.0 * cnt/lag(cnt) over (order by action_type) as prev_ratio
	, first_value(cnt) over() as action_0_cnt
from temp_01 where action_type in ('0', '1', '2', '3', '5', '6')
)
select max(action_0_cnt) as action_0_cnt
	, max(case when action_type='1' then prev_ratio end) as landing_to_product_ratio
	, max(case when action_type='2' then prev_ratio end) as product_to_detail_ratio
	, max(case when action_type='3' then prev_ratio end) as detail_to_cart_ratio
	, max(case when action_type='5' then prev_ratio end) as cart_to_checkout_ratio
	, max(case when action_type='6' then prev_ratio end) as checkout_to_complete_ratio
from temp_02;

-- action_type별 건수, action_type별 고유 세션 건수, 세션별 action type 수행 건수. 
select action_type, count(*) as action_cnt
	, count(distinct sess_id) as sess_cnt_per_action
	, 1.0*count(*)/count(distinct sess_id) as action_cnt_per_sess
from ga_sess_hits 
group by action_type;


select * from ga_sess;

-- 특정 기간중 방문 횟수별 방문 사용자(repeat visitor) 건수 
with
temp_01 as (
select user_id, count(distinct visit_date) as daily_visit_cnt 
from ga_sess
where visit_date between '20160801' and '20160807'
group by user_id
)
select daily_visit_cnt, count(*) as user_cnt 
from temp_01 group by daily_visit_cnt
order by 1;

-- 한달 기간중에 주간 방문 횟수별 사용자 건수
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


-- 첫번째 세션과 반복 세션의 매출 전환율 비교
with 
temp_01 as (
select 
	case when row_number() over (partition by user_id order by visit_stime) = 1 then 'first_session'
	     else 'repeat_session' end as sess_gubun
	, a.*
from ga_sess a
where visit_date between '20160801' and '20160831'
)
select sess_gubun
	, count(distinct a.sess_id) as sess_cnt
	, count(distinct b.order_id) as ord_cnt
	, 1.0 * count(distinct b.order_id)/count(distinct a.sess_id) as conv_rate
	, sum(c.prod_revenue)/count(distinct a.sess_id) as revenue_per_sess
from temp_01 a
	left join orders b on a.sess_id = b.sess_id
	left join order_items c on b.order_id = c.order_id
group by sess_gubun;


-- MAU를 신규 고객, 월별 반복 방문고객, 재 방문 고객건수로 분리하여 추출.
with
temp_01 as ( 
	select a.sess_id, a.user_id, date_trunc('month', a.visit_stime) as visit_month
		, date_trunc('month', b.create_time) as create_month
	from ga_sess a
		join ga_users b on a.user_id = b.user_id 
	where a.visit_stime between to_date('20160801', 'yyyymmdd') and to_date('20161031', 'yyyymmdd')
),
temp_02 as (
	select user_id, visit_month, max(create_month) as create_month
		, lag(visit_month) over (partition by user_id order by visit_month) as prev_visit_month
	from temp_01
	group by user_id, visit_month
),
temp_03 as (
select a.* 
	, case when create_month = visit_month then 'new_user'
		   when (visit_month - interval '1' month) = prev_visit_month then 'repeat_user'
		   else 'comeback_user' end as user_gubun
from temp_02 a
-- where 조건을 삭제하면 전체 user의 visit_month별 접속이 나옴. U0000108 repeat 사용자 경우로 설명. 
-- where 조건을 넣으면 이제 사용자별로 고유한 레벨이 됨. 
where visit_month = to_date('20161001', 'yyyymmdd') 
)
select count(*) as mau
	, sum(case when user_gubun='new_user' then 1 else 0 end) as new_user_cnt
	, sum(case when user_gubun='repeat_user' then 1 else 0 end) as repeat_user_cnt
	, sum(case when user_gubun='comeback_user' then 1 else 0 end) as comeback_user_cnt
from temp_03;


-- MAU를 신규 고객, 월별 반복 방문고객, 재 방문 고객건수로 분리하여 추출.
with
temp_01 as ( 
	select a.sess_id, a.user_id, date_trunc('month', a.visit_stime) as visit_month
		, date_trunc('month', b.create_time) as create_month
	from ga_sess a
		join ga_users b on a.user_id = b.user_id 
	where a.visit_stime between to_date('20160801', 'yyyymmdd') and to_date('20161031', 'yyyymmdd')
),
temp_02 as (
	select user_id, visit_month, max(create_month) as create_month
		, lag(visit_month) over (partition by user_id order by visit_month) as prev_visit_month
	from temp_01
	group by user_id, visit_month
),
temp_03 as (
select a.* 
	, case when create_month = visit_month then 'new_user'
		   when (visit_month - interval '1 month') = prev_visit_month then 'repeat_user'
		   else 'comeback_user' end as user_gubun
from temp_02 a
-- where 조건을 삭제하면 전체 user의 visit_month별 접속이 나옴. U0000108 repeat 사용자 경우로 설명. 
-- where 조건을 넣으면 이제 사용자별로 고유한 레벨이 됨. 
where visit_month = to_date('20161001', 'yyyymmdd') 
)
select count(*) as mau
	, sum(case when user_gubun='new_user' then 1 else 0 end) as new_user_cnt
	, sum(case when user_gubun='repeat_user' then 1 else 0 end) as repeat_user_cnt
	, sum(case when user_gubun='comeback_user' then 1 else 0 end) as comeback_user_cnt
from temp_03;


--  WAU를 주별로 신규 고객, 반복 방문고객, 재 방문 고객건수로 분리하여 추출.
with
temp_01 as ( 
	select a.sess_id, a.user_id, date_trunc('week', a.visit_stime) as visit_week
		, date_trunc('week', b.create_time) as create_week
	from ga_sess a
		join ga_users b on a.user_id = b.user_id 
	where a.visit_stime between to_date('20160801', 'yyyymmdd') and to_date('20161031', 'yyyymmdd')
),
temp_02 as (
	select user_id, visit_week, max(create_week) as create_week
		, lag(visit_week) over (partition by user_id order by visit_week) as prev_visit_week
	from temp_01
	group by user_id, visit_week
),
temp_03 as (
	select a.* 
		, case when create_week = visit_week then 'new_user'
			   when (visit_week - interval '1 week') = prev_visit_week then 'repeat_user'
			   else 'comeback_user' end as user_gubun
	from temp_02 a
where visit_week between to_date('20161001', 'yyyymmdd') and to_date('20161024', 'yyyymmdd')
)
select visit_week, count(*) as mau
	, sum(case when user_gubun='new_user' then 1 else 0 end) as new_user_cnt
	, sum(case when user_gubun='repeat_user' then 1 else 0 end) as repeat_user_cnt
	, sum(case when user_gubun='comeback_user' then 1 else 0 end) as comeback_user_cnt
from temp_03
group by visit_week
order by 1;
