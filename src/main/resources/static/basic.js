//게시글 작성
function post_save() {
    //modal id 확인
    let id = $('#post-modal-id').val()
    let url = '/api/posts'

    //id 값이 있다면 edit
    if (id > 0) {
        url = '/api/posts/' + id;
    }
    let writer = $('#writer').val();
    let password = $('#password').val();
    let title = $('#title').val();
    let contents = $('#contents').val();

    //입력 확인
    if (writer === '') {
        alert("이름을 입력하세요.")
        return;
    }
    if (password === '') {
        alert("암호를 입력하세요.")
        return;
    }
    if (title === '') {
        alert("제목을 입력하세요.")
        return;
    }
    if (contents === '') {
        alert("내용을 입력하세요.")
        return;
    }

    //게시글 생성/수정 요청
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify({
            'writer': writer,
            'password': password,
            'title': title,
            'contents': contents
        }),
        contentType: 'application/json',
        success: function (response) {
            alert("저장이 완료되었습니다.")
            window.location.reload()
        }
    })
}