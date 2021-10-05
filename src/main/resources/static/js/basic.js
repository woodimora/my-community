let page = 1;

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
    window.location.href="/post-form";
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

//p번째 페이지 호출
function get_page(p) {
    page = p - 1;
    get_posts()
}

//게시글 조회 갯수 : 10 개
function get_posts() {
    $('#post-list').empty()
    $('#pagination-ul').empty()
    $.ajax({
        type: 'GET',
        url: `/api/posts?page=${page}&display=10`,
        success: function (response) {
            let nowPage = page + 1;
            let totalPage = response['totalPages'] > 0 ? response['totalPages'] : 1;

            let paginationHtml = '';
            let leftPage = nowPage - 1;
            let rightPage = totalPage - nowPage;
            //좌측 페이지가 4개 이상이면 ...으로 변환
            if (leftPage > 4) {
                paginationHtml = `<li><a class="pagination-link" aria-label="Goto page 1" onclick="get_page(1)">1</a></li>`
                paginationHtml += `<li><span class="pagination-ellipsis">&hellip;</span></li>`
                for (let i = nowPage - 2; i < nowPage; i++) {
                    paginationHtml += `<li><a class="pagination-link" aria-label="Goto page ${i}" onclick="get_page(${i})">${i}</a></li>`
                }
            } else {
                for (let i = 1; i < nowPage; i++) {
                    paginationHtml += `<li><a class="pagination-link" aria-label="Goto page ${i}" onclick="get_page(${i})">${i}</a></li>`
                }
            }

            paginationHtml += `<li><a class="pagination-link is-current" aria-label="Goto page ${nowPage}" onclick="get_page(${nowPage})">${nowPage}</a></li>`

            //우측 페이지가 4개 이상이면 ...으로 변환
            if (rightPage > 4) {
                for (let i = nowPage + 1; i <= nowPage + 2; i++) {
                    paginationHtml += `<li><a class="pagination-link" aria-label="Goto page ${i}" onclick="get_page(${i})">${i}</a></li>`
                }
                paginationHtml += `<li><span class="pagination-ellipsis">&hellip;</span></li>`
                paginationHtml += `<li><a class="pagination-link" aria-label="Goto page ${totalPage}" onclick="get_page(${totalPage})">${totalPage}</a></li>`
            } else {
                for (let i = nowPage + 1; i <= totalPage; i++) {
                    paginationHtml += `<li><a class="pagination-link" aria-label="Goto page ${i}" onclick="get_page(${i})">${i}</a></li>`
                }
            }

            $('#pagination-ul').append(paginationHtml);


            if (response['empty'] === true)
                return;
            let posts = response['content']
            console.log(posts)

            for (let i = 0; i < response['numberOfElements']; i++) {
                let post = posts[i];
                let tempHtml = `
        <div class="box post-card" onclick="get_post_detail(${post['id']})">
            <article class="media">
                <div class="media-left">
                    <figure class="image is-64x64" style="margin: auto;">
                        <img class="is-rounded" src="${post['profileImage']}">
                    </figure>
                </div>
                <div class="media-content">
                    <div class="content">
                        <strong style="font-size: 20px">${post['title']}</strong>
                        <small>@${post['nickname']}</small>
                        <p>
                            <small style="color: gray;">${date2str(post['createdAt'])}</small>
                        </p>
                    </div>
                    <nav class="level is-mobile">
                        <div class="level-left">
                                <small>댓글 : ${post['commentsCount']}</small>
                        </div>
                        <div class="level-right">
                            <small>조회수 : ${post['viewCount']}</small>
                        </div>
                    </nav>
                </div>
            </article>
        </div>`
                $('#post-list').append(tempHtml)
            }
        }
    })
}
//날짜 변환
function date2str(dateStr) {
    // 클라이언트 Timezone offset
    let offset = (new Date()).getTimezoneOffset() * 1000 * 60;
    // 서버에서 받은 시간
    let utcTime = new Date(dateStr)
    // 서버 기준시에서 클라이언트의 Timezone offset 을 뺀 시간
    let day = new Date(utcTime - offset);
    // let day = new Date(dateStr);
    let year = `${day.getFullYear()}`;
    let month = day.getMonth() < 10 ? `0${day.getMonth()}` : `${day.getMonth()}`;
    let date = day.getDate() < 10 ? `0${day.getDate()}` : `${day.getDate()}`;
    let hours = day.getHours() < 10 ? `0${day.getHours()}` : `${day.getHours()}`;
    let minutes = day.getMinutes() < 10 ? `0${day.getMinutes()}` : `${day.getMinutes()}`;
    let seconds = day.getSeconds() < 10 ? `0${day.getSeconds()}` : `${day.getSeconds()}`;

    return `${year}년 ${month}월 ${date}일 ${hours}:${minutes}:${seconds}`
}

function save_post() {
    let title = $('#title').val();
    let contents = $('#contents').val();
    if (title === '') {
        alert("제목을 입력하세요.")
        return;
    }
    if (contents === '') {
        alert("내용을 입력하세요.")
        return;
    }

    //게시글 생성 요청
    $.ajax({
        type: 'POST',
        url: '/api/posts',
        data: JSON.stringify({
            'title': title,
            'contents': contents
        }),
        contentType: 'application/json',
        success: function (response) {
            alert("저장이 완료되었습니다.")
            window.location.replace("/")
        }
    })
}

function get_post_detail(post_id) {
    window.location.href = "/posts/" + post_id;
}