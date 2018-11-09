(* helper methods *)
fun month_of_date(date : int*int*int) =
    #2 date

fun is_older(first : int*int*int, second : int*int*int) =	
    if #1 first < #1 second
    then true
    else if #1 first = #1 second andalso  #2 first < #2 second
    then true
    else if #1 first = #1 second andalso #2 first = #2 second
    then #3 first < #3 second
    else false
	     			   
fun number_in_month(dates : (int*int*int) list, month : int) =
    let
	fun count(d : (int*int*int) list, acc : int) =
	    if null d
	    then acc
	    else if month_of_date (hd d) = month
	    then count(tl d, acc + 1)
	    else count(tl d, acc)		  
    in
	count(dates, 0)
    end

fun number_in_months(dates : (int*int*int) list, months : int list) =
    if null months
    then 0
    else number_in_month(dates, hd months) + number_in_months(dates, (tl months))      

fun dates_in_month(dates : (int*int*int) list, month : int) =
    let
	(* tree recursion due to keep the order *)
	fun accumulate(d : (int*int*int) list) =
	    if null d
	    then []
	    else if month_of_date (hd d) = month
	    then (hd d) :: accumulate(tl(d))
	    else accumulate(tl(d))
    in
	accumulate(dates)
    end
	
fun dates_in_months(dates : (int*int*int) list, months : int list) =
    if null months
    then []
    else dates_in_month(dates, (hd months)) @ dates_in_months(dates, (tl months))

fun get_nth(l : string list, n : int) =
    if n=1
    then hd l
    else get_nth((tl l), n - 1)
		
fun date_to_string(date : int*int*int) =
    let 
	val months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
    in
	get_nth(months, #2 date) ^ " " ^ Int.toString(#3 date) ^ ", " ^ Int.toString(#1 date)
    end

fun number_before_reaching_sum(sum : int, nums : int list) =
    if null nums
    then 0
    else
	let
	    val num = hd nums
	in
	    if num < sum
	    then 1 + number_before_reaching_sum(sum - num, tl nums)
	    else 0
	end

fun what_month(day : int) =
    let
	val days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    in
	number_before_reaching_sum(day, days) + 1
    end

fun month_range(day1 : int, day2 : int) =
    if day1 > day2
    then []
    else
	what_month(day1) :: month_range(day1 + 1, day2)
    
fun oldest(dates : (int*int*int) list) =
    if null dates
    then NONE
    else
	let
	    val left = oldest(tl dates)
	in
	    if isSome left andalso is_older(valOf left, hd dates)
	    then left
	    else SOME(hd dates)
	end
