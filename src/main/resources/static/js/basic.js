function loginForm() {
    window.location.replace("/user/login");
}

function home() {
    window.location.replace("/");
}

function registerForm() {
    window.location.replace("/user/register");
}

function logout() {
    window.location.replace("/user/logout");
}

function postForm() {
    window.location.replace("/posts/form");
}

function login() {
    window.location.replace("/user/login");
}

function login_kakao() {
    window.location.replace("https://kauth.kakao.com/oauth/authorize?client_id=34859b74f2e726356d9d9c561f7a8932&redirect_uri=http://localhost:8080/user/kakao/callback&response_type=code");
}

function check_dup_id() {
    let username = $('#register_id').val();

    //입력 확인
    if (username === '') {
        $('#register_id').addClass('is-danger');
        $('#id-empty-error').removeClass('is-hidden');
        $('#id-dup-error').addClass('is-hidden');
        $('#id-available').addClass('is-hidden');
        return;
    }

    //게시글 생성/수정 요청
    $.ajax({
        type: 'POST',
        url: 'user/login/dup-id',
        data: JSON.stringify({
            'username': username,
        }),
        contentType: 'application/json',
        success: function (response) {
        }
    })
}
function check_dup_nickname() {
    let nickname = $('#register_nickname').val();

    //입력 확인
    if (nickname === '') {
        $('#register_nickname').addClass('is-danger');
        $('#nickname-empty-error').removeClass('is-hidden');
        $('#nickname-dup-error').addClass('is-hidden');
        $('#nickname-available').addClass('is-hidden');
        return;
    }

    //게시글 생성/수정 요청
    $.ajax({
        type: 'POST',
        url: 'user/login/dup-nickname',
        data: JSON.stringify({
            'nickname': nickname,
        }),
        contentType: 'application/json',
        success: function (response) {
        }
    })
}