/*JS cho Giao diện đăng nhập*/

const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});
$('#password, #confirm_password').on('keyup', function() {
    if ($('#password').val() == $('#confirm_password').val()) {
        $('#messageMatch').html('Mật khẩu hợp lệ').css('color', 'green');
    } else
        $('#messageMatch').html('Mật khẩu không khớp').css('color', 'red');
});

function check_pass(e) {
    if (document.getElementById('password').value == document.getElementById('confirm_password').value) {
        return true;
    } else {
        e.preventDefault();
        return false;
    }
}

let counter = document.getElementById('counter');
let count = 0;
let wait = 10;

let id = setInterval(() => {
    count++;
    let remain = wait - count;

    if (remain >= 0) {
        counter.innerHTML = remain;
    } else {
        clearInterval(id);
        window.location.href = 'login.php'
    }
}, 1000)