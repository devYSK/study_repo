$(function () {

    // 구매하기
    $(".purchase-btn").click(function (e) {
        let removeButtons = $(".remove-product-btn");
        if (removeButtons.length <= 0) {
            alert("Cart is empty");
            return;
        }

        const cartId = $( this ).data("cart-id");
        location.href = "/checkout/order-checkout?cartId=" + cartId;
    });

    // 계속 쇼핑하기
    $(".shopping-btn").click(function (e) {
        location.href = "/";
    })

    // 카트에서 삭제
    $(".remove-product-btn").click(function () {
        const cartItemId = $( this ).data("cart-item-id");
        $.ajax({
            url: "/cart/cart-item/" + cartItemId,
            data: { cartItemId: cartItemId },
            method: "DELETE",
            dataType: "json"
        })
            .done(function(json) {
                alert("장바구니에서 삭제하였습니다.");
                location.reload();
            })
            .fail(function(xhr, status, errorThrown) {
                console.log(">>> response, " + xhr.status);
                if (xhr.status == '401') {
                    alert("로그인이 필요합니다.");
                    location.href = "/customer/login";
                }
                else {
                    alert("장바구니에서 상품을 삭제하지 못했습니다.");
                }
            })
    });
})