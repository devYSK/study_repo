$(function () {
    $(".cart-btn").click(function (e) {
        // 장바구니 담기
        const productId = $( this ).data("product-id");
        $.ajax({
            url: "/cart/" + productId,
            data: { productId: productId },
            method: "POST",
            dataType: "json"
        })
            .done(function(json) {
                if (confirm("장바구니에 선택하신 상품을 담았습니다. 장바구니로 이동하시겠습니까?")) {
                    location.href = "/cart"
                };
            })
            .fail(function(xhr, status, errorThrown) {
                console.log(">>> response, " + xhr.status);
                if (xhr.status == '401') {
                    alert("로그인이 필요합니다.");
                    location.href = "/customer/login";
                }
                else {
                    alert("장바구니에 상품을 담지 못했습니다. 다시 시도해보시기 바랍니다.");
                }
            })
        console.log("Product Id = " + productId);
    });

    $(".purchase-btn").click(function () {
        const productId = $( this ).data("product-id");
        console.log("Product Id = " + productId);
        if (confirm("상품을 구매하시겠습니까?")) {
            location.href = "/checkout/direct-checkout?productId=" + productId;
        };
    });
});