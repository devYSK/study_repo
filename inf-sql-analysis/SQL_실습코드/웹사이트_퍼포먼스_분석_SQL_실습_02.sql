
/************************************
사용자 생성 날짜 별 일주일간 잔존율(Retention rate) 구하기
*************************************/
with temp_01 as (
	select a.user_id, date_trunc('day', a.create_time)::date as user_create_date,  date_trunc('day', b.visit_stime)::date as sess_visit_date
		, count(*) cnt
	from ga_users a
		left join ga_sess b
			on a.user_id = b.user_id
	where  create_time >= (:current_date - interval '8 days') and create_time < :current_date
	group by a.user_id, date_trunc('day', a.create_time)::date, date_trunc('day', b.visit_stime)::date
),
temp_02 as (
select user_create_date, count(*) as create_cnt
	-- d1 에서 d7 일자별 접속 사용자 건수 구하기. 
	, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else 0 end ) as d1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else 0 end) as d2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else 0 end) as d3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else 0 end) as d4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else 0 end) as d5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else 0 end) as d6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else 0 end) as d7_cnt
	/*
	, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else null end ) as d1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else null end) as d2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else null end) as d3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else null end) as d4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else null end) as d5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else null end) as d6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else null end) as d7_cnt
	*/
from temp_01 
group by user_create_date
)
select user_create_date, create_cnt
     -- d1 에서 d7 일자별 잔존율 구하기.
	, round(100.0 * d1_cnt/create_cnt, 2) as d1_ratio
	, round(100.0 * d2_cnt/create_cnt, 2) as d2_ratio
	, round(100.0 * d3_cnt/create_cnt, 2) as d3_ratio
	, round(100.0 * d4_cnt/create_cnt, 2) as d4_ratio
	, round(100.0 * d5_cnt/create_cnt, 2) as d5_ratio
	, round(100.0 * d6_cnt/create_cnt, 2) as d6_ratio
	, round(100.0 * d7_cnt/create_cnt, 2) as d7_ratio
from temp_02 order by 1;

/************************************
주별 잔존율(Retention rate) 및 주별 특정 채널 잔존율
*************************************/
with temp_01 as (
	select a.user_id, date_trunc('week', a.create_time)::date as user_create_date,  date_trunc('week', b.visit_stime)::date as sess_visit_date
		, count(*) cnt
	from ga_users a
		left join ga_sess b
			on a.user_id = b.user_id
	--where  create_time >= (:current_date - interval '7 weeks') and create_time < :current_date
	where create_time >= to_date('20160912', 'yyyymmdd') and create_time < to_date('20161101', 'yyyymmdd')
	group by a.user_id, date_trunc('week', a.create_time)::date, date_trunc('week', b.visit_stime)::date
), 
temp_02 as (
select user_create_date, count(*) as create_cnt
     -- w1 에서 w7까지 주단위 접속 사용자 건수 구하기.
	, sum(case when sess_visit_date = user_create_date + interval '1 week' then 1 else null end ) as w1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 week' then 1 else null end) as w2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 week' then 1 else null end) as w3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 week' then 1 else null end) as w4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 week' then 1 else null end) as w5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 week' then 1 else null end) as w6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 week' then 1 else null end) as w7_cnt
from temp_01 
group by user_create_date
)
select user_create_date, create_cnt
    -- w1 에서 w7 주별 잔존율 구하기.
	, round(100.0 * w1_cnt/create_cnt, 2) as w1_ratio
	, round(100.0 * w2_cnt/create_cnt, 2) as w2_ratio
	, round(100.0 * w3_cnt/create_cnt, 2) as w3_ratio
	, round(100.0 * w4_cnt/create_cnt, 2) as w4_ratio
	, round(100.0 * w5_cnt/create_cnt, 2) as w5_ratio
	, round(100.0 * w6_cnt/create_cnt, 2) as w6_ratio
	, round(100.0 * w7_cnt/create_cnt, 2) as w7_ratio
from temp_02 order by 1;

-- 주 단위 특정 채널 잔존율(Retention rate)
with temp_01 as (
	select a.user_id, date_trunc('week', a.create_time)::date as user_create_date,  date_trunc('week', b.visit_stime)::date as sess_visit_date
		, count(*) cnt
	from ga_users a
		left join ga_sess b
			on a.user_id = b.user_id
	--where  create_time >= (:current_date - interval '7 weeks') and create_time < :current_date
	where create_time >= to_date('20160912', 'yyyymmdd') and create_time < to_date('20161101', 'yyyymmdd')
	and channel_grouping='Referral' -- Social Organic Search, Direct, Referral
	group by a.user_id, date_trunc('week', a.create_time)::date, date_trunc('week', b.visit_stime)::date
), 
temp_02 as (
select user_create_date, count(*) as create_cnt
	-- w1 에서 w7까지 주단위 접속 사용자 건수 구하기.
	, sum(case when sess_visit_date = user_create_date + interval '1 week' then 1 else null end ) as w1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 week' then 1 else null end) as w2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 week' then 1 else null end) as w3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 week' then 1 else null end) as w4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 week' then 1 else null end) as w5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 week' then 1 else null end) as w6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 week' then 1 else null end) as w7_cnt
from temp_01 
group by user_create_date
)
select user_create_date, create_cnt
     -- w1 에서 w7 주별 잔존율 구하기.
	, round(100.0 * w1_cnt/create_cnt, 2) as w1_ratio
	, round(100.0 * w2_cnt/create_cnt, 2) as w2_ratio
	, round(100.0 * w3_cnt/create_cnt, 2) as w3_ratio
	, round(100.0 * w4_cnt/create_cnt, 2) as w4_ratio
	, round(100.0 * w5_cnt/create_cnt, 2) as w5_ratio
	, round(100.0 * w6_cnt/create_cnt, 2) as w6_ratio
	, round(100.0 * w7_cnt/create_cnt, 2) as w7_ratio
from temp_02 order by 1;

/************************************
 (2016년 9월 12일 부터) 일주일간 생성된 사용자들에 대해 채널별 주 단위 잔존율(Retention rate)
*************************************/
with temp_01 as (
	select a.user_id, channel_grouping
		, date_trunc('week', a.create_time)::date as user_create_date,  date_trunc('week', b.visit_stime)::date as sess_visit_date
		, count(*) cnt
	from ga_users a
		left join ga_sess b
			on a.user_id = b.user_id
	where  create_time >= to_date('20160912', 'yyyymmdd') and create_time < to_date('20160919', 'yyyymmdd')
	--and channel_grouping='Referral' -- Social Organic Search, Direct, Referral
	group by a.user_id, channel_grouping, date_trunc('week', a.create_time)::date, date_trunc('week', b.visit_stime)::date
), 
temp_02 as (
select user_create_date, channel_grouping, count(*) as create_cnt
     -- w1 에서 w7까지 주단위 접속 사용자 건수 구하기.
	, sum(case when sess_visit_date = user_create_date + interval '1 week' then 1 else null end ) as w1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 week' then 1 else null end) as w2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 week' then 1 else null end) as w3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 week' then 1 else null end) as w4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 week' then 1 else null end) as w5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 week' then 1 else null end) as w6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 week' then 1 else null end) as w7_cnt
from temp_01 
group by user_create_date, channel_grouping
)
select user_create_date, channel_grouping, create_cnt
    -- w1 에서 w7 주별 잔존율 구하기
	, round(100.0 * w1_cnt/create_cnt, 2) as w1_ratio
	, round(100.0 * w2_cnt/create_cnt, 2) as w2_ratio
	, round(100.0 * w3_cnt/create_cnt, 2) as w3_ratio
	, round(100.0 * w4_cnt/create_cnt, 2) as w4_ratio
	, round(100.0 * w5_cnt/create_cnt, 2) as w5_ratio
	, round(100.0 * w6_cnt/create_cnt, 2) as w6_ratio
	, round(100.0 * w7_cnt/create_cnt, 2) as w7_ratio
from temp_02 order by 3 desc;

/************************************
 7일간 생성된 총 사용자를 기반으로 총 잔존율을 구하고, 7일간 일별 잔존율을 함께 구하기 
*************************************/
-- 7일간 생성된 총 사용자를 기반으로 총 잔존율을 구하고, 7일간 일별 잔존율을 함께 구하기 
with temp_01 as (
	select a.user_id, date_trunc('day', a.create_time) as user_create_date,  date_trunc('day', b.visit_stime) as sess_visit_date
		, count(*) cnt
	from ga_users a
		left join ga_sess b
			on a.user_id = b.user_id
	where  create_time >= (:current_date - interval '8 days') and create_time < :current_date
	group by a.user_id, date_trunc('day', a.create_time), date_trunc('day', b.visit_stime)
),
temp_02 as (
select user_create_date, count(*) as create_cnt
	, sum(case when sess_visit_date = user_create_date + interval '1 day' then 1 else null end ) as d1_cnt
	, sum(case when sess_visit_date = user_create_date + interval '2 day' then 1 else null end) as d2_cnt
	, sum(case when sess_visit_date = user_create_date + interval '3 day' then 1 else null end) as d3_cnt
	, sum(case when sess_visit_date = user_create_date + interval '4 day' then 1 else null end) as d4_cnt
	, sum(case when sess_visit_date = user_create_date + interval '5 day' then 1 else null end) as d5_cnt
	, sum(case when sess_visit_date = user_create_date + interval '6 day' then 1 else null end) as d6_cnt
	, sum(case when sess_visit_date = user_create_date + interval '7 day' then 1 else null end) as d7_cnt
from temp_01
group by user_create_date
)
-- 7일간 생성된 총 사용자를 기반으로 총 잔존율을 구하기
select 'All User' as user_create_date, sum(create_cnt) as create_cnt
	, round(100.0 * sum(d1_cnt)/sum(create_cnt), 2) as d1_ratio
	, round(100.0 * sum(d2_cnt)/sum(create_cnt), 2) as d2_ratio
	, round(100.0 * sum(d3_cnt)/sum(create_cnt), 2) as d3_ratio
	, round(100.0 * sum(d4_cnt)/sum(create_cnt), 2) as d4_ratio
	, round(100.0 * sum(d5_cnt)/sum(create_cnt), 2) as d5_ratio
	, round(100.0 * sum(d6_cnt)/sum(create_cnt), 2) as d6_ratio
	, round(100.0 * sum(d7_cnt)/sum(create_cnt), 2) as d7_ratio
from temp_02
union all
-- 7일간 일별 잔존율
select to_char(user_create_date, 'yyyy-mm-dd') as user_create_date, create_cnt
	, round(100.0 * d1_cnt/create_cnt, 2) as d1_ratio
	, round(100.0 * d2_cnt/create_cnt, 2) as d2_ratio
	, round(100.0 * d3_cnt/create_cnt, 2) as d3_ratio
	, round(100.0 * d4_cnt/create_cnt, 2) as d4_ratio
	, round(100.0 * d5_cnt/create_cnt, 2) as d5_ratio
	, round(100.0 * d6_cnt/create_cnt, 2) as d6_ratio
	, round(100.0 * d7_cnt/create_cnt, 2) as d7_ratio
from temp_02 order by 1;


/**********************************************
 전체 매출 전환율 및 일별, 월별 매출 전환율과 매출액
***********************************************/
/* 
   Unknown = 0. (홈페이지)
   Click through of product lists = 1, (상품 목록 선택)
   Product detail views = 2, (상품 상세 선택)
   Add product(s) to cart = 3, (카트에 상품 추가)
   Remove product(s) from cart = 4, (카트에서 상품 제거)
   Check out = 5, (결재 시작)
   Completed purchase = 6, (구매 완료)
   Refund of purchase = 7, (환불)
   Checkout options = 8 (결재 옵션 선택)
   
   이 중 1, 3, 4가 주로 EVENT로 발생. 0, 2, 5, 6은 주로 PAGE로 발생. 
 *
 **/

-- action_type별 hit_type에 따른 건수
select action_type, count(*) action_cnt
	, sum(case when hit_type='PAGE' then 1 else 0 end) as page_action_cnt
	, sum(case when hit_type='EVENT' then 1 else 0 end) as event_action_cnt
from ga.ga_sess_hits
group by action_type
;

-- 전체 매출 전환율
with 
temp_01 as ( 
select count(distinct sess_id) as purchase_sess_cnt
from ga.ga_sess_hits
where action_type = '6'
),
temp_02 as ( 
select count(distinct sess_id) as sess_cnt
from ga.ga_sess_hits
)
select a.purchase_sess_cnt, b.sess_cnt, 100.0* a.purchase_sess_cnt/sess_cnt as sale_cv_rate
from temp_01 a 
	cross join temp_02 b
;


-- 과거 1주일간 매출 전환률
with 
temp_01 as ( 
select count(distinct a.sess_id) as purchase_sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
where a.action_type = '6'
and b.visit_stime >= (:current_date - interval '7 days') and b.visit_stime < :current_date
),
temp_02 as ( 
select count(distinct a.sess_id) as sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
and b.visit_stime >= (:current_date - interval '7 days') and b.visit_stime < :current_date
)
select a.purchase_sess_cnt, b.sess_cnt, 100.0* a.purchase_sess_cnt/sess_cnt as sale_cv_rate
from temp_01 a 
	cross join temp_02 b
;


-- 과거 1주일간 일별 매출 전환률 - 01
with 
temp_01 as ( 
select date_trunc('day', b.visit_stime)::date as cv_day, count(distinct a.sess_id) as purchase_sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
where a.action_type = '6'
and b.visit_stime >= (:current_date - interval '7 days') and b.visit_stime < :current_date
group by date_trunc('day', b.visit_stime)::date
), 
temp_02 as ( 
select date_trunc('day', b.visit_stime)::date as cv_day, count(distinct a.sess_id) as sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
where b.visit_stime >= (:current_date - interval '7 days') and b.visit_stime < :current_date
group by date_trunc('day', b.visit_stime)::date
)
select a.cv_day, a.purchase_sess_cnt, b.sess_cnt, 100.0* a.purchase_sess_cnt/sess_cnt as sale_cv_rate
from temp_01 a 
	join temp_02 b on a.cv_day = b.cv_day
;

-- 과거 1주일간 일별 매출 전환률 - 02
with 
temp_01 as ( 
select date_trunc('day', b.visit_stime)::date as cv_day
	, count(distinct a.sess_id) as sess_cnt
	, count(distinct case when a.action_type = '6' then a.sess_id end) as purchase_sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
and b.visit_stime >= (:current_date - interval '7 days') and b.visit_stime < :current_date
group by date_trunc('day', b.visit_stime)::date
)
select a.cv_day, purchase_sess_cnt, sess_cnt, 100.0* purchase_sess_cnt/sess_cnt as sale_cv_rate
from temp_01 a 
;

-- 과거 1주일간 일별 매출 전환률 및 매출액
with 
temp_01 as ( 
select date_trunc('day', b.visit_stime)::date as cv_day
	, count(distinct a.sess_id) as sess_cnt
	, count(distinct case when a.action_type = '6' then a.sess_id end) as purchase_sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
and b.visit_stime >= (:current_date - interval '7 days') and b.visit_stime < :current_date
group by date_trunc('day', b.visit_stime)::date
),
temp_02 as ( 
select date_trunc('day', a.order_time)::date as ord_day
	, sum(prod_revenue) as sum_revenue
from ga.orders a
	join ga.order_items b on a.order_id = b.order_id
where a.order_time >= (:current_date - interval '7 days') and a.order_time < :current_date 
group by date_trunc('day', a.order_time)::date
)
select a.cv_day, b.ord_day, a.sess_cnt, a.purchase_sess_cnt, 100.0* purchase_sess_cnt/sess_cnt as sale_cv_rate
	, b.sum_revenue, b.sum_revenue/a.purchase_sess_cnt as revenue_per_purchase_sess
from temp_01 a
	left join temp_02 b on a.cv_day = b.ord_day
;

	
-- 월별 매출 전환률과 매출액
with 
temp_01 as ( 
select date_trunc('month', b.visit_stime)::date as cv_month
	, count(distinct a.sess_id) as sess_cnt
	, count(distinct case when a.action_type = '6' then a.sess_id end) as purchase_sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
group by date_trunc('month', b.visit_stime)::date
),
temp_02 as ( 
select date_trunc('month', a.order_time)::date as ord_month
	, sum(prod_revenue) as sum_revenue
from ga.orders a
	join ga.order_items b on a.order_id = b.order_id 
group by date_trunc('month', a.order_time)::date
)
select a.cv_month, b.ord_month, a.sess_cnt, a.purchase_sess_cnt, 100.0* purchase_sess_cnt/sess_cnt as sale_cv_rate
	, b.sum_revenue, b.sum_revenue/a.purchase_sess_cnt as revenue_per_purchase_sess
from temp_01 a
	left join temp_02 b on a.cv_month = b.ord_month
;

/************************************
채널별 월별 매출 전환율
*************************************/
with 
temp_01 as ( 
select b.channel_grouping, date_trunc('month', b.visit_stime)::date as cv_month
	, count(distinct a.sess_id) as sess_cnt
	, count(distinct case when a.action_type='6' then a.sess_id end) as pur_sess_cnt
from ga.ga_sess_hits a
	join ga.ga_sess b on a.sess_id = b.sess_id
group by b.channel_grouping, date_trunc('month', b.visit_stime)::date
),
temp_02 as (
select a.channel_grouping, date_trunc('month', b.order_time)::date as ord_month
	, sum(prod_revenue) as sum_revenue
from ga.ga_sess a 
	join ga.orders b on a.sess_id = b.sess_id 
	join ga.order_items c on b.order_id = c.order_id
group by a.channel_grouping, date_trunc('month', b.order_time)::date
)
select a.channel_grouping, a.cv_month, a.pur_sess_cnt, a.sess_cnt
	, round(100.0* pur_sess_cnt/sess_cnt, 2) as sale_cv_rate
	, b.ord_month, round(b.sum_revenue::numeric, 2) as sum_revenue
	, round(b.sum_revenue::numeric/pur_sess_cnt, 2) as rev_per_pur_sess
from temp_01 a
	left join temp_02 b on a.channel_grouping = b.channel_grouping and a.cv_month = b.ord_month
order by 1, 2
;

/************************************
 월별 신규 사용자의 매출 전환율
*************************************/

-- 월별 신규 사용자 건수 
with 
temp_01 as (
select a.sess_id, a.user_id, a.visit_stime, b.create_time
	, case when date_trunc('day', b.create_time)::date >= date_trunc('month', visit_stime)::date 
	       and date_trunc('day', b.create_time)::date < date_trunc('month', visit_stime)::date + interval '1 month'
	  then 1 else 0 end as is_monthly_new_user
from ga.ga_sess a
	join ga.ga_users b on a.user_id = b.user_id
)
select date_trunc('month', visit_stime)::date, count(*) as sess_cnt
	, count(distinct user_id) as user_cnt
	, sum(case when is_monthly_new_user = 1 then 1 end) as new_user_sess_cnt
	, count(distinct case when is_monthly_new_user = 1 then user_id end) as new_user_cnt
from temp_01 
group by date_trunc('month', visit_stime)::date;

-- 월별 신규 사용자의 매출 전환율 - 01
with 
temp_01 as (
select a.sess_id, a.user_id, a.visit_stime, b.create_time
	, case when date_trunc('day', b.create_time)::date >= date_trunc('month', visit_stime)::date 
	       and date_trunc('day', b.create_time)::date < date_trunc('month', visit_stime)::date + interval '1 month'
	  then 1 else 0 end as is_monthly_new_user
from ga.ga_sess a
	join ga.ga_users b on a.user_id = b.user_id
),
-- 매출 전환한 월별 신규 생성자 세션 건수
temp_02 as (
select date_trunc('month', a.visit_stime)::date as cv_month, count(distinct b.sess_id) as purchase_sess_cnt
from temp_01 a
	join ga.ga_sess_hits b on a.sess_id = b.sess_id
where a.is_monthly_new_user = 1
and b.action_type = '6'
group by date_trunc('month', a.visit_stime)::date
),
-- 월별 신규 생성자 세션 건수
temp_03 as (
select date_trunc('month', visit_stime)::date as cv_month
	, sum(case when is_monthly_new_user = 1 then 1 else 0 end) as monthly_nuser_sess_cnt
from temp_01
group by date_trunc('month', visit_stime)::date
)
select a.cv_month, a.purchase_sess_cnt, b.monthly_nuser_sess_cnt
	, 100.0 * a.purchase_sess_cnt/b.monthly_nuser_sess_cnt as sale_cv_rate
from temp_02 a
	join temp_03 b on a.cv_month = b.cv_month
order by 1;

-- 월별 신규 사용자의 매출 전환율 - 02
-- 매출 전환한 월별 신규 생성자 세션 건수와 월별 신규 생성자 세션 건수를 같이 구함.
with 
temp_01 as (
select a.sess_id, a.user_id, a.visit_stime, b.create_time
	, case when date_trunc('day', b.create_time)::date >= date_trunc('month', visit_stime)::date 
	       and date_trunc('day', b.create_time)::date < date_trunc('month', visit_stime)::date + interval '1 month'
	  then 1 else 0 end as is_monthly_new_user
from ga.ga_sess a
	join ga.ga_users b on a.user_id = b.user_id
),
-- 매출 전환한 월별 신규 생성자 세션 건수와 월별 신규 생성자 세션 건수를 같이 구함. 
temp_02 as (
select date_trunc('month', a.visit_stime)::date as cv_month
	, count(distinct case when is_monthly_new_user = 1 and b.action_type = '6' then b.sess_id end ) as purchase_sess_cnt
	, count(distinct case when is_monthly_new_user = 1 then b.sess_id end ) as monthly_nuser_sess_cnt
from temp_01 a
	join ga.ga_sess_hits b on a.sess_id = b.sess_id
group by date_trunc('month', a.visit_stime)::date
)
select a.cv_month, a.purchase_sess_cnt, a.monthly_nuser_sess_cnt
	, 100.0 * a.purchase_sess_cnt/a.monthly_nuser_sess_cnt as sale_cv_rate
from temp_02 a
order by 1;

/************************************
 전환 퍼널(conversion funnel) 구하기
*************************************/
/* 
   Unknown = 0. (홈페이지)
   Click through of product lists = 1, (상품 목록 선택)
   Product detail views = 2, (상품 상세 선택)
   Add product(s) to cart = 3, (카트에 상품 추가)
   Remove product(s) from cart = 4, (카트에서 상품 제거)
   Check out = 5, (결재 시작)
   Completed purchase = 6, (구매 완료)
   Refund of purchase = 7, (환불)
   Checkout options = 8 (결재 옵션 선택)
   
   이 중 1, 3, 4가 주로 EVENT로 발생. 0, 2, 5, 6은 주로 PAGE로 발생. 
 *
 **/

select * from ga.ga_sess_hits
where sess_id = 'S0213506'
order by hit_seq;

-- 1주일간 세션 히트 데이터에서 세션별로 action_type의 중복 hit를 제거하고 세션별 고유한 action_type만 추출
drop table if exists ga.temp_funnel_base;

create table ga.temp_funnel_base
as
select * 
from (
	select a.*, b.visit_stime, b.channel_grouping 
		, row_number() over (partition by a.sess_id, action_type order by hit_seq) as action_seq
	from ga.ga_sess_hits a
		join ga.ga_sess b on a.sess_id = b.sess_id
	where visit_stime >= (to_date('2016-10-31', 'yyyy-mm-dd') - interval '7 days') and visit_stime < to_date('2016-10-31', 'yyyy-mm-dd')
	) a where a.action_seq = 1
;


-- action_type 전환 퍼널 세션 수 구하기(FLOW를 순차적으로 수행한 전환 퍼널)
-- action_type 0 -> 1 -> 2 -> 3 -> 5 -> 6 순으로의 전환 퍼널 세션 수를 구함.  
with 
temp_act_0 as ( 
select sess_id, hit_type, action_type
from ga.temp_funnel_base a
where a.action_type = '0'
), 
temp_hit_02 as ( 
select a.sess_id as home_sess_id
	, b.sess_id as plist_sess_id
	, c.sess_id as pdetail_sess_id
	, d.sess_id as cart_sess_id
	, e.sess_id as check_sess_id
	, f.sess_id as pur_sess_id
from temp_act_0 a
	left join ga.temp_funnel_base b on (a.sess_id = b.sess_id and b.action_type = '1')
	left join ga.temp_funnel_base c on (b.sess_id = c.sess_id and c.action_type = '2')
	left join ga.temp_funnel_base d on (c.sess_id = d.sess_id and d.action_type = '3')
	left join ga.temp_funnel_base e on (d.sess_id = e.sess_id and e.action_type = '5')
	left join ga.temp_funnel_base f on (e.sess_id = f.sess_id and f.action_type = '6')
)
select count(home_sess_id) as home_sess_cnt
	, count(plist_sess_id) as plist_sess_cnt
	, count(pdetail_sess_id) as pdetail_sess_cnt
	, count(cart_sess_id) as cart_sess_cnt
	, count(check_sess_id) as check_sess_cnt
	, count(pur_sess_id) as purchase_sess_cnt
from temp_hit_02
;

-- action_type 전환 퍼널 세션 수 구하기(FLOW를 스킵한 세션까지 포함한 전환 퍼널)
with
temp_01 as (
select count(sess_id) as home_sess_cnt
from temp_funnel_base
where action_type = '0'
),
temp_02 as (
select count(sess_id) as plist_sess_cnt
from temp_funnel_base
where action_type = '1'
),
temp_03 as (
select count(sess_id) as pdetail_sess_cnt
from temp_funnel_base
where action_type = '2'
),
temp_04 as (
select count(sess_id) as cart_sess_cnt
from temp_funnel_base
where action_type = '3'
),
temp_05 as (
select count(sess_id) as check_sess_cnt
from temp_funnel_base
where action_type = '5'
),
temp_06 as (
select count(sess_id) as purchase_sess_cnt
from temp_funnel_base
where action_type = '6'
)
select home_sess_cnt, plist_sess_cnt, pdetail_sess_cnt
	, cart_sess_cnt, check_sess_cnt, purchase_sess_cnt
from temp_01
	cross join temp_02
	cross join temp_03
	cross join temp_04
	cross join temp_05
	cross join temp_06
;


-- 채널별 action_type 전환 퍼널 세션 수 구하기
-- 채널별로 action_type 0 -> 1 -> 2 -> 3 -> 6 순으로의 전환 퍼널 세션 수를 구함.
with 
temp_act_0 as ( 
select sess_id, hit_type, action_type, channel_grouping
from ga.temp_funnel_base a
where a.action_type = '0'
), 
temp_hit_02 as ( 
select a.sess_id as home_sess_id, a.channel_grouping as home_cgrp
	, b.sess_id as plist_sess_id, b.channel_grouping as plist_cgrp
	, c.sess_id as pdetail_sess_id, c.channel_grouping as pdetail_cgrp
	, d.sess_id as cart_sess_id, d.channel_grouping as cart_cgrp
	, e.sess_id as check_sess_id, e.channel_grouping as check_cgrp
	, f.sess_id as pur_sess_id, f.channel_grouping as pur_cgrp
from temp_act_0 a
	left join ga.temp_funnel_base b on (a.sess_id = b.sess_id and b.action_type = '1')
	left join ga.temp_funnel_base c on (b.sess_id = c.sess_id and c.action_type = '2')
	left join ga.temp_funnel_base d on (c.sess_id = d.sess_id and d.action_type = '3')
	left join ga.temp_funnel_base e on (d.sess_id = e.sess_id and e.action_type = '5')
	left join ga.temp_funnel_base f on (e.sess_id = f.sess_id and f.action_type = '6')
)
select home_cgrp
 	, count(home_sess_id) as home_sess_cnt
	, count(plist_sess_id) as plist_sess_cnt
	, count(pdetail_sess_id) as pdetail_sess_cnt
	, count(cart_sess_id) as cart_sess_cnt
	, count(check_sess_id) as check_sess_cnt
	, count(pur_sess_id) as purchase_sess_cnt
from temp_hit_02
group by home_cgrp
;