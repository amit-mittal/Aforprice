/*
 * This file contains all the methods to
 * configure price alerts
 */

//add Price Alert
function priceAlert(ID,price){

	 var id = '#' + ID; 
	//console.log(price);
	//alert(id);
	var p = $(id);
	var pos = p.position();	

	$("#login_box").css({left:pos.left,top:pos.top-100});
	$('form[name="login"]').find('input[name="price"]').val(price);
	$('form[name="login"]').find('input[name="productId"]').val(ID);
}

$('div.btn-group').click(function(){
	//alert($("input[@name=is_private]:checked").val())   
	$('.toHide').hide();
	$("#blk-"+$("input[@name=is_private]:checked").val()).show('slow');
});