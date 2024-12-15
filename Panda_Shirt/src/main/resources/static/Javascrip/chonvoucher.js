
let timeout;
$(document).on('input', '#mavoucher-input', function () {
    clearTimeout(timeout);
    var voucherId = $(this).val();
    console.log("Giá trị voucherId: ", voucherId);
    if (voucherId.trim() === "") {
        console.log("Voucher ID không hợp lệ");
        return;
    }
    timeout = setTimeout(function () {
        nhapvoucher(voucherId);
    }, 1500);
});

function nhapvoucher(voucherId) {
    console.log("Voucher ID: ", voucherId);
    $.ajax({
        url: "/panda/banhangoffline/selectvc",
        type: "GET",
        data: {
            id: voucherId
        },
        success: function (response) {
            $("#mavoucher-input").val(response.mavocher);
            $("#idvoucher-input").val(response.idvoucher);
            $("#mucgiam-input").val(response.mucgiam);
            $("#mucgiam").text(response.mucgiam);
            $("#thanhtien").text('Tổng tiền : ' + response.thanhtien);
            $("#thanhTien").val(response.thanhTien);
            $('#voucherModal').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi gọi AJAX:", error);
            const errorResponse = JSON.parse(xhr.responseText);
            if (errorResponse.error) {
                Swal.fire({
                    title: 'Thông báo',
                    text:errorResponse,
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        }
    });
}

function chonVoucher(element) {
    var voucherId = $(element).data('id');
    console.log("Voucher ID: ", voucherId);
    $.ajax({
        url: "/panda/banhangoffline/selectvc",
        type: "GET",
        data: {
            id: voucherId
        },
        success: function (response) {
            $("#mavoucher-input").val(response.mavocher);
            $("#idvoucher-input").val(response.idvoucher);
            $("#mucgiam-input").val(response.mucgiam);
            $("#mucgiam").text(response.mucgiam);
            // $("#mucgiam").text(response.loai);
            $("#thanhtien").text('Tổng tiền : ' + response.thanhtien);
            $("#thanhTien").val(response.thanhTien);
            $("#thanhtien-input").val(response.thanhTien);
            $('#voucherModal').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi gọi AJAX:", error);
            const errorResponse = JSON.parse(xhr.responseText);
            if (errorResponse.error) {
                // alert(errorResponse.error);
                Swal.fire({
                    title: 'Thông báo',
                    text: errorResponse.error,
                    icon: 'error',
                    confirmButtonText: 'OK'
                })
            }
        }
    });
}

function chonkh(element) {
    var idkh = $(element).data('id');
    console.log("kh ID: ", idkh);
    $.ajax({
        url: "/panda/banhangoffline/chonkh",
        type: "GET",
        data: {
            id: idkh
        },
        success: function (response) {
            $("#tenkh-input").val(response.tenkh);
            $("#idkh-input").val(response.id);
            $("#sdt-input").val(response.sdt);
            $("#diachi-input").val(response.diachi);

            // $("#selectedProvince").val(response.idTinhThanhPho);
            // $("#selectedDistrict").val(response.idQuanHuyen);
            // $("#selectedWard").val(response.idXaPhuong);

            // $("#tentinh").val(response.tentinh);
            // $("#tenhuyen").val(response.tenhuyen);
            // $("#tenxa").val(response.tenxa);

            $('#chonkhachhangModal').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi gọi AJAX:", error);
            const errorResponse = JSON.parse(xhr.responseText);
            if (errorResponse.error) {
                Swal.fire({
                    title: 'Thông báo',
                    text:errorResponse,
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        }
    });
}


// function chonkh(element) {
//     var idkh = $(element).data('id');
//     localStorage.removeItem("selectedCustomer");
//     axios.get(`/panda/banhangoffline/chonkh?id=${idkh}`)
//         .then(response => {
//             const data = response.data;
//             console.log("Địa chỉ : ", data);
//             localStorage.setItem("selectedCustomer", JSON.stringify({
//                 idTinhThanhPho: data.idTinhThanhPho,
//                 idQuanHuyen: data.idQuanHuyen,
//                 idXaPhuong: data.idXaPhuong
//             }));
//
//             $("#province").val(data.idTinhThanhPho).trigger("change");
//             // Gọi API để load Quận/Huyện theo Tỉnh/Thành phố
//             callApiDistrict(`https://provinces.open-api.vn/api/p/${data.idTinhThanhPho}?depth=2`)
//                 .then(() => {
//                     // Cập nhật giá trị Quận/Huyện
//                     $("#district").val(data.idQuanHuyen).trigger("change");
//
//                     // Gọi API để load Xã/Phường theo Quận/Huyện
//                     callApiWard(`https://provinces.open-api.vn/api/d/${data.idQuanHuyen}?depth=2`)
//                         .then(() => {
//                             // Cập nhật giá trị Xã/Phường
//                             $("#ward").val(data.idXaPhuong);
//                         });
//                 });
//             $('#chonkhachhangModal').modal('hide');
//         })
//         .catch(error => {
//             console.error("Error", error);
//         });
// }

