/* SCRIPT Tương Tác với SQL và hiển thị các hộp thoại thông báo */
var selectedAccount = 0;
$(document).ready(function() {
    getAccounts();
    setAddAccountOnClickListener();
});

function setAddAccountOnClickListener() {
    $(".add-account").click(function() {
        var nameValue = $('#username').val().trim();
        var emailValue = $('#email').val().trim();
        var passwordValue = $('#password').val().trim();
        var statusValue = $('#status').val().trim();
        if (nameValue === '' || emailValue === '' || passwordValue === '' || statusValue === '') {
            showDialog('text-warning', 'Cảnh báo', 'Vui lòng nhập đầy đủ các thông tin.')
        } else {
            $.post("them-thecao.php", {
                    username: nameValue,
                    email: emailValue,
                    password: passwordValue,
                    status: statusValue
                },
                function(data, status) {
                    if (data.status === true) {
                        getAccounts();
                    } else {
                        showDialog('text-warning', 'Cảnh báo', 'Không thể kết nối tới cơ sở dữ liệu.')
                    }
                },
            );
        };
    });
}

function getAccounts() {
    $.get("get-thecao.php", function(response, status) {
        let tableBody = ``;
        response.data.forEach(element => {
            tableBody +=
                `<tr>
                            <td>` + element.id + `</td>			
                            <td>` + element.mathecao + `</td>
                            <td>` + element.soseri + `</td>
                            <td>` + element.menhgia + `</td>
							<td>` + element.trangthai + `</td>
							
                            <td>
                                <a class="btn btn-sm btn-primary" href="#" onclick="conformEdit(` + element.id + `, '` + element.mathecao + `', '` + element.soseri + `', '` + element.menhgia + `','` + element.trangthai + `')">Sửa</a> 
                                <a id="remove-all-btn" class="btn btn-sm btn-danger" href="#" onclick="confirmRemoval(` + element.id + `, '` + element.username + `')">Xóa</a>
                            </td>
                        </tr>`;
        });
        $('#table-body').html(tableBody);
    }, "json");
};

function confirmRemoval(id, username) {
    selectedAccount = id;
    $('#confirm-removal-modal').modal({ show: true });
};

function remove() {
    $.post("xoa-thecao.php", {
            id: selectedAccount,
        },
        function(data, status) {
            if (data.status === true) {
                showDialog('text-success', 'Thành công', 'Xoá thẻ cào thành công.');
                setTimeout(() => {
                    $('#dialog').fadeOut("slow").modal('hide');
                }, 3000);
                getAccounts();
            } else {
                showDialog('text-danger', 'Thất bại', 'Có lỗi xảy ra, vui lòng kiểm tra lại.');
            }
        },
    );
};

function conformEdit(id, username, email, password, status) {
    selectedAccount = id;

    document.getElementById("name_update").value = username;
    document.getElementById("email_update").value = email;
    document.getElementById("password_update").value = password;
    document.getElementById("status_update").value = status;
    $('#confirm-edit-modal').modal({ show: true });
};

function update() {
    const nameToUpdate = document.getElementById("name_update").value
    const emailToUpdate = document.getElementById("email_update").value
    const passwordToUpdated = document.getElementById("password_update").value
    const statusToUpdated = document.getElementById("status_update").value
    $.post("capnhat-thecao.php", {
            id: selectedAccount,
            username: nameToUpdate,
            email: emailToUpdate,
            password: passwordToUpdated,
            status: statusToUpdated,
        },
        function(data, status) {
            if (data.status) {
                showDialog('text-success', 'Thành công', 'Cập nhật thẻ cào thành công.');
                setTimeout(() => {
                    $('#dialog').fadeOut("slow").modal('hide');
                }, 3000);
                getAccounts();
            } else {
                showDialog('text-primary', 'Thông tin', 'Không có thông tin thay đổi.');
                setTimeout(() => {
                    $('#dialog').fadeOut("slow").modal('hide');
                }, 3000);
            }
        },
    );
}

function showDialog(modify, title, message) {
    $('#dialog-header h4').removeClass();
    $('#dialog').modal({ show: true });
    $('#dialog-header h4').html(title);
    $('#dialog-header h4').addClass(modify);
    $('.modal-body-message').html(message);
}