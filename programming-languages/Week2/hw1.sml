fun is_older(first : int*int*int, second : int*int*int) =
    if #1 first < #1 second
    then true
    else
	if #2 first < #2 second
	then true
	else #3 first < #3 second
			   
fun month_of_date(date : int*int*int) =
    #2 date	    
    
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
    let
	fun count(m : int list, acc : int) =
	    if null m
	    then acc
	    else count(tl(m), acc + number_in_month(dates, hd m))
    in
	count(months, 0)
    end
	
fun dates_in_month(dates : (int*int*int) list, month : int) =
    let
	fun accumulate(d : (int*int*int) list, acc : (int*int*int) list) =
	    if null d
	    then acc
	    else if month_of_date (hd d) = month
	    then accumulate(tl(d), acc :: (hd d))
	    else accumulate(tl(d), acc)
    in
	accumulate(dates, [])
    end
	
