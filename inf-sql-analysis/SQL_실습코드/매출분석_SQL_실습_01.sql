/************************************
   일/주/월/분기별 매출액 및 주문 건수
*************************************/
select date_trunc('day', order_date)::date as day
	, sum(amount) as sum_amount, count(distinct a.order_id) as daily_ord_cnt
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
group by date_trunc('day', order_date)::date 
order by 1;

/************************************
   일/주/월/분기별 상품별 매출액 및 주문 건수
*************************************/
select date_trunc('day', order_date)::date as day, b.product_id
	, sum(amount) as sum_amount, count(distinct a.order_id) as daily_ord_cnt
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
group by date_trunc('day', order_date)::date, b.product_id
order by 1, 2;


/************************************
월별 상품카테고리별 매출액 및 주문 건수, 월 전체 매출액 대비 비율
step 1: 상품 카테고리 별 월별 매출액 추출
step 2: step 1의 집합에서 전체 매출액을 analytic으로 구한 뒤에 매출액 비율 계산. 
*************************************/
with 
temp_01 as (
select d.category_name, to_char(date_trunc('month', order_date), 'yyyymm') as month_day
	, sum(amount) as sum_amount, count(distinct a.order_id) as monthly_ord_cnt
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
	join nw.products c on b.product_id = c.product_id 
    join nw.categories d on c.category_id = d.category_id
group by d.category_name, to_char(date_trunc('month', order_date), 'yyyymm')
)
select *
	, sum(sum_amount) over (partition by month_day) as month_tot_amount
	, round(sum_amount / sum(sum_amount) over (partition by month_day), 3) as month_ratio
from temp_01;

/************************************
상품별 전체 매출액 및 해당 상품 카테고리 전체 매출액 대비 비율, 해당 상품카테고리에서 매출 순위
step 1: 상품별 전체 매출액을 구함
step 2: step 1의 집합에서 상품 카테고리별 전체 매출액을 구하고, 비율과 매출 순위를 계산. 
*************************************/
with
temp_01 as ( 
	select a.product_id, max(product_name) as product_name, max(category_name) as category_name
		, sum(amount) as sum_amount
	from nw.order_items a
		join nw.products b
			on a.product_id = b.product_id
		join nw.categories c 
			on b.category_id = c.category_id
	group by a.product_id
)
select product_name, sum_amount as product_sales
	, category_name
	, sum(sum_amount) over (partition by category_name) as category_sales
	, sum_amount / sum(sum_amount) over (partition by category_name) as product_category_ratio
	, row_number() over (partition by category_name order by sum_amount desc) as product_rn
from temp_01
order by category_name, product_sales desc;



/************************************
동년도 월별 누적 매출 및 동일 분기 월별 누적 매출
step 1: 월별 매출액을 구한다 
step 2: 월별 매출액 집합에 동일 년도의 월별 누적 매출과 동일 분기의 월별 누적 매출을 구함. 
*************************************/
with 
temp_01 as (
select date_trunc('month', order_date)::date as month_day
	, sum(amount) as sum_amount
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
group by date_trunc('month', order_date)::date
)
select month_day, sum_amount
	, sum(sum_amount) over (partition by date_trunc('year', month_day) order by month_day) as cume_year_amount
	, sum(sum_amount) over (partition by date_trunc('quarter', month_day) order by month_day) as cume_quarter_amount
from temp_01;

/************************************************
 * 5일 이동 평균 매출액 구하기. 매출액의 경우 주로 1주일 이동 평균 매출을 구하나 데이터가 토,일 매출이 없음.  
 *************************************************/
with 
temp_01 as (
select date_trunc('day', order_date)::date as d_day
	, sum(amount) as sum_amount
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
where order_date >= to_date('1996-07-08', 'yyyy-mm-dd')
group by date_trunc('day', order_date)::date
)
select d_day, sum_amount
	, avg(sum_amount) over (order by d_day rows between 4 preceding and current row) as m_avg_5day
from temp_01;

-- 5일 이동 평균을 구하되 5일을 채울 수 없는 경우는 Null로 표시
with 
temp_01 as (
select date_trunc('day', order_date)::date as d_day
	, sum(amount) as sum_amount
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
where order_date >= to_date('1996-07-08', 'yyyy-mm-dd')
group by date_trunc('day', order_date)::date
),
temp_02 as (
select d_day, sum_amount
	, avg(sum_amount) over (order by d_day rows between 4 preceding and current row) as m_avg_5days
	, row_number() over (order by d_day) as rnum
from temp_01
)
select d_day, sum_amount, rnum
	, case when rnum < 5 then Null
	       else m_avg_5days end as m_avg_5days
from temp_02;

/************************************************
 * 5일 이동 가중평균 매출액 구하기. 당일 날짜에서 가까운 날짜일 수록 가중치를 증대. 
 5일중 가장 먼 날짜는 매출액의 0.5, 중간 날짜 2, 3, 4는 매출액 그대로, 당일은 1.5 * 매출액으로 가중치 부여
 *************************************************/
with 
temp_01 as (
select date_trunc('day', order_date)::date as d_day
	, sum(amount) as sum_amount
	, row_number() over (order by date_trunc('day', order_date)::date) as rnum
from nw.orders a
	join nw.order_items b on a.order_id = b.order_id
where order_date >= to_date('1996-07-08', 'yyyy-mm-dd')
group by date_trunc('day', order_date)::date
),
temp_02 as (
select a.d_day, a.sum_amount, a.rnum, b.d_day as d_day_back, b.sum_amount as sum_amount_back, b.rnum as rnum_back 
from temp_01 a
	join temp_01 b on a.rnum between b.rnum and b.rnum + 4 
	--join temp_01 b on b.rnum between a.rnum - 4 and a.rnum;
)
select d_day
	, avg(sum_amount_back) as m_avg_5days
	-- sum을 건수인 5로 나누면 평균이 됨. 
	, sum(sum_amount_back)/5 as m_avg_5days_01
	-- 가중 이동 평균을 구하기 위해 가중치 값에 따라 sum을 구함. 
	, sum(case when rnum - rnum_back = 4 then 0.5 * sum_amount_back
	           when rnum - rnum_back in (3, 2, 1) then sum_amount_back
	           when rnum - rnum_back = 0 then 1.5 * sum_amount_back 
	      end) as m_weighted_sum
	-- 위에서 구한 가중치 값에 따른 sum을 5로 나눠서 가중 이동 평균을 구함. 
	, sum(case when rnum - rnum_back = 4 t해en 0.5 * sum_amount_back
		   when rnum - rnum_back in (3, 2, 1) then sum_amount_back
		   when rnum - rnum_back = 0 then 1.5 * sum_amount_back 
	      end) / 5 as m_w_avg_sum
        -- 5건이 안되는 초기 데이터는 삭제하기 위해서임.  
	, count(*) as cnt
from temp_02
group by d_day
having count(*) = 5
order by d_day
;