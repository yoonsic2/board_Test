
// index.js

function goHome() {
    // 로그인 여부에 따라 변경
    location.href = "/";	// localhost:80
}


function msgPrint() {
    if(m!=null) {
        alert(m)
    }
}
function loginStatus(){
    if(mb){
        $('#m_name').html(`${mb.m_name} 님`)
        $('.suc').css('display', 'block');
        $('.bef').css('display', 'none');
    }else {
        $('.suc').css('display', 'none');
        $('.bef').css('display', 'block');
    }
}
