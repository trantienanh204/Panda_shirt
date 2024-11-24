let timeout;
$(document).on('input', '#mavoucher-input', function() {
    clearTimeout(timeout);
    var voucherId = $(this).val();
    console.log("Giá trị voucherId: ", voucherId);
    if (voucherId.trim() === "") {
        console.log("Voucher ID không hợp lệ");
        return;
    }
    timeout = setTimeout(function() {
        nhapvoucher(voucherId);
    }, 1500);
});

function nhapvoucher(voucherId) {
    console.log("Voucher ID: ", voucherId);
    var phishipValue = $("#phiship").val();
    $.ajax({
        url: "/panda/banhangoffline/selectvc",
        type: "GET",
        data: {
            id: voucherId,
            phiship: phishipValue
        },
        success: function(response) {
            $("#mavoucher-input").val(response.mavocher);
            $("#idvoucher-input").val(response.idvoucher);
            $("#mucgiam").text(response.mucgiam);
            // $("#mucgiam").text(response.loai);
            $("#thanhtien").text('Tổng tiền : ' +response.thanhtien);
            $("#thanhTien").val(response.thanhTien);
            $('#voucherModal').modal('hide');
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi gọi AJAX:", error);
            const errorResponse = JSON.parse(xhr.responseText);
            if (errorResponse.error) {
                alert(errorResponse.error);
            }
        }
    });
}

function chonVoucher(element) {
    var voucherId = $(element).data('id');
    var phishipValue = $("#phiship").val();
    console.log("Voucher ID: ", voucherId);
    $.ajax({
        url: "/panda/banhangoffline/selectvc",
        type: "GET",
        data: {
            id: voucherId,
            phiship: phishipValue
        },
        success: function(response) {
            $("#mavoucher-input").val(response.mavocher);
            $("#idvoucher-input").val(response.idvoucher);
            $("#mucgiam").text(response.mucgiam);
            // $("#mucgiam").text(response.loai);
            $("#thanhtien").text('Tổng tiền : ' +response.thanhtien);
            $("#thanhTien").val(response.thanhTien);
            $('#voucherModal').modal('hide');
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi gọi AJAX:", error);
            const errorResponse = JSON.parse(xhr.responseText);
            if (errorResponse.error) {
                alert(errorResponse.error);
            }
        }
    });
}
// $(document).ready(function() {
//     var initialVoucherId = $("#mavoucher-input").val();
//
//     $("#mavoucher-input").on("input", function() {
//         var voucherId = $(this).val();
//         var phishipValue = $("#phiship").val();
//
//         if (!voucherId && initialVoucherId) {
//             $.ajax({
//                 url: "/panda/banhangoffline/selectvc",
//                 type: "GET",
//                 data: {
//                     id: "",
//                     phiship: phishipValue
//                 },
//                 success: function(response) {
//                     $("#mavoucher-input").val(response.mavocher);
//                     $("#idvoucher-input").val(response.idvoucher);
//                     $("#mucgiam").text(response.mucgiam);
//                     $("#thanhtien").text('Tổng tiền : ' + response.thanhtien);
//                     $("#thanhTien").val(response.thanhTien);
//                     $('#voucherModal').modal('hide');
//                 },
//                 error: function(xhr, status, error) {
//                     console.error("Lỗi khi gọi API:", error);
//                 }
//             });
//         }
//         else if (voucherId) {
//             $.ajax({
//                 url: "/panda/banhangoffline/selectvc",
//                 type: "GET",
//                 data: {
//                     id: voucherId,
//                     phiship: phishipValue
//                 },
//                 success: function(response) {
//                     $("#mavoucher-input").val(response.mavocher);
//                     $("#idvoucher-input").val(response.idvoucher);
//                     $("#mucgiam").text(response.mucgiam);
//                     $("#thanhtien").text('Tổng tiền : ' + response.thanhtien);
//                     $("#thanhTien").val(response.thanhTien);
//                     $('#voucherModal').modal('hide');
//                 },
//                 error: function(xhr, status, error) {
//                     console.error("Lỗi khi gọi API:", error);
//                 }
//             });
//         }
//         initialVoucherId = voucherId;
//     });
// });
$(document).ready(function() {
    $("#province").change(function() {
        var voucherId = $("#mavoucher-input").val();
        var phishipValue = $("#phiship").val();
        $.ajax({
            url: "/panda/banhangoffline/selectvc",
            type: "GET",
            data: {
                id: voucherId,
                phiship: phishipValue
            },
            success: function(response) {
                $("#mavoucher-input").val(response.mavocher);
                $("#idvoucher-input").val(response.idvoucher);
                $("#mucgiam").text(response.mucgiam);
                $("#thanhtien").text('Tổng tiền : ' +response.thanhtien);
                $("#thanhTien").val(response.thanhTien);
                $('#voucherModal').modal('hide');
            },
            error: function(xhr, status, error) {
                console.error("Lỗi khi gọi API:", error);
            }
        });
    });
});

$('#voucherModal').on('shown.bs.modal', function () {
    $(this).find('input, button, a').first().focus();
});

$('#voucherModal').on('hidden.bs.modal', function () {
    $('#openModalButton').focus();
})
