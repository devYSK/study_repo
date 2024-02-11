$(function(){
    $("#signupForm").submit(function () {
        let username = $("#username").val();
        let email = $("#email").val();
        let password1 = $("#password1").val();
        let password2 = $("#password2").val();

        if (username == "") {
            alert("이름을 입력해주세요");
            return false;
        }

        if (email == "") {
            alert("이메일을 입력해주세요");
            return false;
        }

        if (password1 == "") {
            alert("비밀번호를 입력해주세요");
            return false;
        }

        if (password2 == "") {
            alert("비밀번호를 입력해주세요");
            return false;
        }
    });
});