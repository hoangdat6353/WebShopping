/* SCRIPT Tương Tác với SQL và hiển thị các hộp thoại thông báo */

var selectedAccount = 0;
$(document).ready(function() {
    getAccounts();

});



function getAccounts() {
    $.get("get-theloai.php", function(response, status) {
        let tableBody = ``;
        response.data.forEach(element => {
            tableBody +=
                `<tr>
                            <td>` + element.id + `</td>			
                            <td>` + element.tentheloai + `</td>
                            <td>` + element.soluong + `</td>                        						
                            <td>
                                <a class="btn btn-sm btn-primary" href="#" onclick="conformEdit(` + element.id + `, '` + element.tentheloai + `', '` + element.soluong + `')">Sửa</a> 
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
    $.post("xoa-theloai.php", {
            id: selectedAccount,
        },
        function(data, status) {
            if (data.status === true) {
                showDialog('text-success', 'Thành công', 'Xoá thể loại thành công.');
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

function conformEdit(id, username) {
    selectedAccount = id;

    document.getElementById("name_update").value = username;
    document.getElementById("categoriesId").value = selectedAccount;

    $('#confirm-edit-modal').modal({ show: true });
};




function showDialog(modify, title, message) {
    $('#dialog-header h4').removeClass();
    $('#dialog').modal({ show: true });
    $('#dialog-header h4').html(title);
    $('#dialog-header h4').addClass(modify);
    $('.modal-body-message').html(message);
}