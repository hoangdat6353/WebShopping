/* SCRIPT Tương Tác với SQL và hiển thị các hộp thoại thông báo */

var selectedAccount = 0;
$(document).ready(function() {
    getAccounts();

});

function getAccounts() {
    $.get("get-accounts.php", function(response, status) {
        let tableBody = ``;
        response.data.forEach(element => {
            tableBody +=
                `<tr>
                    <td>` + element.id + `</td>			
                    <td>` + element.username + `</td>
                    <td>` + element.email + `</td>
                    <td>` + element.password + `</td>
                    <td>` + element.quyen + `</td>
                    <td>
                        <a class="btn btn-sm-2 btn-primary" href="#" onclick="conformEdit(` + element.id + `, '` + element.username + `', '` + element.email + `', '` + element.password + `')">Sửa</a> 
                        <a id="remove-all-btn" class="btn btn-sm-2 btn-danger" href="#" onclick="confirmRemoval(` + element.id + `, '` + element.username + `')">Xóa</a>
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
    $.post("delete-account.php", {
            id: selectedAccount,
        },
        function(data, status) {
            if (data.status === true) {
                showDialog('text-success', 'Thành công', 'Xoá tài khoản thành công.');
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

function conformEdit(id, username, email, password) {
    selectedAccount = id;

    document.getElementById("name_update").value = username;
    document.getElementById("email_update").value = email;

    $('#confirm-edit-modal').modal({ show: true });
};

function update() {
    const nameToUpdate = document.getElementById("name_update").value
    const emailToUpdate = document.getElementById("email_update").value

    $.post("update-account.php", {
            id: selectedAccount,
            username: nameToUpdate,
            email: emailToUpdate,
        },
        function(data, status) {
            // TODO: === vs ==
            if (data.status) {
                showDialog('text-success', 'Thành công', 'Cập nhật tài khoản thành công.');
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