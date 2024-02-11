$(function (){
   $("#register-account").click(function() {
       let username = $("#username").val();
       let password1 = $("#password1").val();
       let password2 = $("#password2").val();

       if(username == '') {
           alert("이름을 입력하세요.")
           return;
       }

       if(password1 == '' || password2 == '') {
           alert("비밀번호를 입력하세요.")
           return;
       }

       if(password1 != password2) {
           alert("비밀번호가 서로 다릅니다.")
           return;
       }

       $("#register-form").submit();
   });
});