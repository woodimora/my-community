function loginForm() {
    window.location.href="/user/login";
}

function home() {
    window.location.href="/";
}

function registerForm() {
    window.location.href="/user/register";
}

function logout() {
    window.location.href="/user/logout";
}

function postForm() {
    window.location.href="/posts/form";
}

function login_kakao() {
    window.location.href="https://kauth.kakao.com/oauth/authorize?client_id=34859b74f2e726356d9d9c561f7a8932&redirect_uri=http://localhost:8080/user/kakao/callback&response_type=code";
}

function check_dup_id() {
    let username = $('#register_id').val();

    //입력 확인
    if (!is_username(username)) {
        $('#register_id').addClass('is-danger');
        $('#id-error').removeClass('is-hidden');
        $('#id-dup-error').addClass('is-hidden');
        $('#id-available').addClass('is-hidden');
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/user/register/dup-id',
        data: JSON.stringify({
            'username': username,
        }),
        contentType: 'application/json',
        success: function (response) {
            if (response === "success") {
                $('#register_id').removeClass('is-danger');
                $('#id-error').addClass('is-hidden');
                $('#id-dup-error').addClass('is-hidden');
                $('#id-available').removeClass('is-hidden');
            } else {
                $('#register_id').addClass('is-danger');
                $('#id-error').addClass('is-hidden');
                $('#id-dup-error').removeClass('is-hidden');
                $('#id-available').addClass('is-hidden');
            }
        }
    })
}

function check_dup_nickname() {
    let nickname = $('#register_nickname').val();

    //입력 확인
    if (!is_nickname(nickname)) {
        $('#register_nickname').addClass('is-danger');
        $('#nickname-dup-error').addClass('is-hidden');
        $('#nickname-error').removeClass('is-hidden');
        $('#nickname-available').addClass('is-hidden');
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/user/register/dup-nickname',
        data: JSON.stringify({
            'nickname': nickname,
        }),
        contentType: 'application/json',
        success: function (response) {
            if (response === "success") {
                $('#register_nickname').removeClass('is-danger');
                $('#nickname-dup-error').addClass('is-hidden');
                $('#nickname-error').addClass('is-hidden');
                $('#nickname-available').removeClass('is-hidden');
            } else {
                $('#register_nickname').addClass('is-danger');
                $('#nickname-dup-error').removeClass('is-hidden');
                $('#nickname-error').addClass('is-hidden');
                $('#nickname-available').addClass('is-hidden');
            }
        }
    })
}

function register() {
    let username = $('#register_id').val();
    let nickname = $('#register_nickname').val();
    let password1 = $('#register_password1').val();
    let password2 = $('#register_password2').val();
    let email = $('#register_email').val();
    let profile_image = $('#register_profile_image').attr('src');

    if($('#id-available').hasClass('is-hidden')){
        $('#register_id').addClass('is-danger');
        $('#id-error').removeClass('is-hidden');
        $('#id-dup-error').addClass('is-hidden');
        $('#id-available').addClass('is-hidden');
        return;
    }

    if($('#nickname-available').hasClass('is-hidden')){
        $('#register_nickname').addClass('is-danger');
        $('#nickname-dup-error').addClass('is-hidden');
        $('#nickname-error').removeClass('is-hidden');
        $('#nickname-available').addClass('is-hidden');
        return;
    }

    if (!is_username(username)) {
        $('#register_id').addClass('is-danger');
        $('#id-error').removeClass('is-hidden');
        $('#id-dup-error').addClass('is-hidden');
        $('#id-available').addClass('is-hidden');
        return;
    }

    if (!is_nickname(nickname)) {
        $('#register_nickname').addClass('is-danger');
        $('#nickname-dup-error').addClass('is-hidden');
        $('#nickname-error').removeClass('is-hidden');
        $('#nickname-available').addClass('is-hidden');
        return;
    }

    if (!is_password(password1)) {
        $('#register_password1').addClass('is-danger');
        $('#password1-error').removeClass('is-hidden');
        return;
    }

    if (password1 !== password2) {
        $('#register_password2').addClass('is-danger');
        $('#password2-error').removeClass('is-hidden');
        return;
    }

    if (!is_email(email)) {
        $('#register_email').addClass('is-danger');
        $('#email-error').removeClass('is-hidden');
        $('#email-dup-error').addClass('is-hidden');
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/user/register',
        data: JSON.stringify({
            'username': username,
            'nickname': nickname,
            'password': password1,
            'email': email,
            'profileImage': profile_image
        }),
        contentType: 'application/json',
        success: function (request) {
            alert("회원가입이 되었습니다.")
            window.location.replace("/")
        },
        error: function (response,status,error) {
            let errorMsg = response['responseJSON']['errorMessage'];
            if(errorMsg === '중복된 아이디를 사용하는 사용자가 존재합니다.') {
                $('#register_id').addClass('is-danger');
                $('#id-dup-error').removeClass('is-hidden');
                $('#id-error').removeClass('is-hidden');
                $('#id-available').addClass('is-hidden');
            }
            else if(errorMsg === '중복된 별명를 사용하는 사용자가 존재합니다.') {
                $('#register_nickname').addClass('is-danger');
                $('#nickname-dup-error').removeClass('is-hidden');
                $('#nickname-error').removeClass('is-hidden');
                $('#nickname-available').addClass('is-hidden');
            }
            else if(errorMsg === '중복된 이메일를 사용하는 사용자가 존재합니다.') {
                $('#register_email').addClass('is-danger');
                $('#email-error').addClass('is-hidden');
                $('#email-dup-error').removeClass('is-hidden');
            }
        }
    })
}

function is_username(asValue) {
    var regExp = /^(?=.*[a-zA-Z])[-a-zA-Z0-9_.]{2,16}$/;
    return regExp.test(asValue);
}

function is_password(asValue) {
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{12,32}$/;
    return regExp.test(asValue);
}

function is_email(asValue) {
    var regExp = /^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
    return regExp.test(asValue);
}

function is_nickname(asValue) {
    var regExp = /^[가-힣a-zA-Z0-9_.]{2,10}$/;
    return regExp.test(asValue);
}