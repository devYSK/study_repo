$(function () {
    $("#checkout-form").submit(function () {
        let selectedPayType = $("#pay-type").val();
        let payTypes = new Array("CREDIT_CARD", "CASH");
        let notAvailablePayType = !(payTypes.indexOf(selectedPayType) > -1);
        if (notAvailablePayType) {
            alert("결제 수단을 선택해주세요.");
            $("#pay-type").focus();
            return false;
        }
    });
});