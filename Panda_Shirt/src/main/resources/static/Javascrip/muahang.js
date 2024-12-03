
$(document).ready(function () {
    function checkCart() {
        const tableBody = $('#productTable tbody');
        const productTable = $('#productTable');
        const emptyCartMessage = $('#emptyCartMessage');

        if (tableBody.children().length === 0) {
            productTable.hide();
            emptyCartMessage.show();
        } else {
            productTable.show();
            emptyCartMessage.hide();
        }
    }

    $('#searchInput').on('input', function () {
        const keyword = $(this).val().trim();
        if (keyword.length >= 1) {
            $.ajax({
                url: `/panda/banhangoffline/find`,
                method: 'GET',
                data: {keyword: keyword},
                success: function (data) {
                    $('#productList').empty();
                    if (data.length >= 1) {
                        data.forEach(function (item) {
                            const parts = item.split(' - ');
                            const product = {
                                id: parts[0],
                                tensp: parts[1],
                                mausac: parts[2],
                                kichthuoc: parts[3],
                                dongia: parts[4],
                            };
                            $('#productList').append(
                                `<li class="list-group-item product-item" data-product='${JSON.stringify(product)}'>
                        ${product.id} -   ${product.tensp} - ${product.mausac} - ${product.kichthuoc}</li>`
                            );
                        });
                    } else {
                        $('#productList').append(
                            `<li class="list-group-item">Không tìm thấy sản phẩm</li>`
                        );
                    }
                },
                error: function () {
                    alert('Lỗi tìm sản phẩm!');
                }
            });
        } else {
            $('#productList').empty();
        }
    });

// $(document).on('click', '.product-item', function () {
//     event.preventDefault();
//     const product = $(this).data(''); // Lấy sản phẩm từ data attribute
//     const productId = product.id; // Lấy ID từ sản phẩm
//     const quantityInput = $(`tr[data-id="${product.id}"] .so-luong input`); // Lấy input số lượng
//     const quantity = parseInt(quantityInput.val()) || 1; // Lấy số lượng (mặc định là 1 nếu không có)
//     const totalPrice = quantity * product.dongia;
//
//     if (productId) {
//         $('#productList').empty();
//         $('#searchInput').val('');
//     } else {
//         console.error('Product ID is not defined');
//     }
//     $.ajax({
//         url: `/panda/banhangoffline/taohdct`,
//         method: 'POST',
//         contentType: 'application/json',
//         data: JSON.stringify({
//             idSanPhamCT: product.id,
//             donGia: product.dongia,
//             soLuong: quantity,
//             thanhTien: totalPrice
//         }),
//         success: function (response) {
//             console.log('Dữ liệu gửi thành công:', response);
//             location.reload();
//         }
//     });
// });


    function addToCart(product) {
        const tableBody = $('#productTable tbody');
        const existingRow = tableBody.find(`tr[data-id="${product.id}"]`);
        console.log(existingRow);
        if (existingRow.length > 0) {
            // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng lên 1
            const soluong = existingRow.find('.so-luong input'); // Lấy ô input để tăng số lượng
            const tongsl = parseInt(soluong.val());
            soluong.val(tongsl + 1); // Cập nhật giá trị của ô input
            updatetongtien(existingRow, product.dongia);
        } else {
            const newRow = `
        <tr th:data-id="${product.id}">
            <td>${product.tensp}</td>
            <td>${product.mausac}</td>
            <td>${product.kichthuoc}</td>
            <td>${product.dongia}</td>
            <td class="so-luong"><input type="number" value="1" min="1"
             style="width: 70px;border-radius: 10px;text-align: center"></td>
            <td class="tong-tien">${product.dongia}</td>
            <td><button class="btn btn-danger remove-btn">
            <i class="fa-solid fa-trash"></i></button></td>
        </tr>
    `;
            tableBody.append(newRow);
        }
        soluongthaydoi(tableBody.find(`tr[data-id="${product.id}"]`), product.dongia);
        checkCart();
    }

    function soluongthaydoi(row, dongia) {
        row.find('.so-luong input').off('input').on('input', function () {
            let soluong = $(this).val();

            // if (soluong.empty()) {
            //      soluong = 1; // Nếu ô trống, đặt lại thành 1
            // } else
            if (isNaN(soluong) || parseInt(soluong) === 0) {
                soluong = 1; // Nếu không phải là số hoặc < 1, đặt lại thành 1
            } else {
                soluong = parseInt(soluong);
            }
            $(this).val(soluong);

            updatetongtien(row, dongia);
        });
    }

    let previousValue = 0;

    $(document).on('focus', '.quantity-input', function () {
        previousValue = $(this).val(); // Lưu giá trị cũ của ô input
    });

    $(document).on('input', '.quantity-input', function () {
        const inputElement = $(this); // Lấy phần tử input đã thay đổi
        const idHoaDonCT = inputElement.data('id'); // Lấy ID từ data-id
        const productId = inputElement.data('product');// Lấy ID từ data-product
        const row = $(this).closest('tr'); // Lấy hàng tương ứng
        const dongia = parseFloat(row.find('td:nth-child(4)').text());
        const soluong = parseInt($(this).val()) || 1;
        console.log('ID HoaDonCT: ', idHoaDonCT);
        console.log('ID spct: ', productId);
        const thanhtien = dongia * soluong;
        row.find('.tong-tien').text(thanhtien.toFixed(2));
        $.ajax({
            url: '/panda/banhangoffline/update',
            type: 'POST', // Phương thức POST
            contentType: 'application/json',
            data: JSON.stringify({
                idSanPhamCT: productId,
                idHoadon: parseInt(idHoaDonCT),
                soLuong: soluong,
                thanhTien: thanhtien
            }),
            success: function (response) {
                console.log('Cập nhật thành công:', response);
                setTimeout(function () {
                    location.reload();
                }, 1000);
            },
            error: function (xhr) {
                // Xử lý lỗi từ server
                const errorMessage = xhr.responseText || 'Có lỗi xảy ra';
                alert(errorMessage);
                Swal.fire({
                    title: 'Thông báo báo!',
                    text: errorMessage,
                    icon: 'warning',
                    confirmButtonText: 'OK'
                });

                inputElement.val(previousValue);

                // Hiển thị lại thành tiền cũ
                const previousTotal = dongia * previousValue;
                row.find('.tong-tien').text(previousTotal.toFixed(2));
            }
        });
    });

    function updatetongtien(row, dongia) {
        const soluong = parseInt(row.find('.so-luong input').val());
        const tongtien = soluong * dongia;
        row.find('.tong-tien').text(tongtien);
    }


    checkCart();
});

function setActiveTab(tab) {
    var navLinks = document.querySelectorAll('#invoiceTabs .nav-link');

    navLinks.forEach(function (link) {
        link.classList.remove('active');
    });

    tab.classList.add('active');
}

$(document).ajaxSend(function (event, xhr, settings) {
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    xhr.setRequestHeader(csrfHeader, csrfToken);
});

const initialProductList = document.getElementById('product-list-container').innerHTML;

function updateProductList() {
    let keyword = document.getElementById("searchsanpham").value;

    if (!keyword) {
        keyword = '';
    }

    if (keyword.trim() === '') {
        document.getElementById('product-list-container').innerHTML = initialProductList;
        return;
    }
    fetch(`http://localhost:8080/panda/banhangoffline/search?keyword=${encodeURIComponent(keyword)}`)
        .then(response => response.json())
        .then(data => {
            console.log("Dữ liệu trả về từ API:", data);
            updateProductListUI(data);
        })
        .catch(error => {
            console.error('Error fetching product list:', error);
        });
}

function updateProductListUI(products) {
    const productListContainer = document.getElementById('product-list-container');
    if (products.length === 0) {
        productListContainer.innerHTML = '<p>Không có sản phẩm phù hợp </p>';
        return;
    }

    let productHTML = "";
    products.forEach((item, index) => {
        const parts = item.split(' - ');
        const spct = {
            id: parts[0],
            tensp: parts[1],
            mausac: parts[2],
            kichthuoc: parts[3],
            dongia: parts[4],
            tenChatLieu: parts[5],
            soluongsanpham: parts[6],
            hinhanh: parts[6],
        };
        productHTML += `    
            <div class="product-row" style="display: flex; gap: 10px; border-bottom: 1px solid #ddd; padding: 10px;">
                <input type="hidden" name="idspct" value="${spct.id}">
                <input type="hidden" name="dongia" value="${spct.dongia}">
                <div class="product-price" style="display: flex; align-items: center; justify-content: center;">
                    <h4 style="font-size: 16px; font-weight: bold;">${index + 1}</h4>
                </div>
                <div class="product-image" style="flex: 1; text-align: center;">
                    <img th:src="@{/Image/panda_logo.png}" alt="Sản phẩm" style="max-width: 120px; height: auto;">
                </div>
                <div class="product-details" style="flex: 2; text-align: left; font-size: 14px;">
                    <h4 style="font-size: 17px; font-weight: bold;">${spct.tensp} [ ${spct.mausac} - ${spct.kichthuoc} ]</h4>
                    <p style="margin: 0;font-size: 15px">Màu: <span>${spct.mausac}</span> - Kích cỡ: <span>${spct.kichthuoc}</span></p>
                    <p style="margin: 0;font-size: 15px">Chất liệu: <span>${spct.tenChatLieu}</span></p>
                    <span style="display: inline-block; background-color: #d4fcd4; color: #008000; border: 1px solid #c2e5c2; padding: 5px 10px; border-radius: 15px; font-size: 14px; font-weight: bold; text-align: center;">
                        Đang kinh doanh
                    </span>
                </div>
                <div class="product-price" style="flex: 1; display: flex; align-items: center; justify-content: center; flex-direction: column; color: red; font-size: 16px;">
                    <span style="font-weight: bold; color: black">${spct.soluongsanpham}</span>
                </div>
                <div class="product-price" style="flex: 1; display: flex; align-items: center; justify-content: center; flex-direction: column; font-size: 16px;">
                    <span style="font-weight: bold;">${spct.dongia}</span>
                </div>
                <div class="product-action" style="flex: 1; display: flex; align-items: center; justify-content: center;">
                    <button class="btn-dark"  
                    style="background-color: #FE5621; color: white; padding: 5px 15px; border: none; cursor: pointer; border-radius: 5px;">
                        Chọn
                    </button>
                </div>
            </div>
        `;

    });
    productListContainer.innerHTML = productHTML;
}

$(document).on('click', '.product-action button', function (event) {
    event.preventDefault();

    // Lấy giá trị idspct từ input hidden trong cùng phần tử cha
    const productId = $(this).closest('.product-row').find('input[name="idspct"]').val();
    const productdongia = $(this).closest('.product-row').find('input[name="dongia"]').val();

    if (productId) {
        console.log('ID Sản phẩm:', productId);

        // Tiến hành các hành động khác, ví dụ như gọi API để thêm vào giỏ hàng hoặc xử lý khác
        $.ajax({
            url: '/panda/banhangoffline/taohdct',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                idSanPhamCT: productId,
                donGia: productdongia,
                soLuong: 1,
                thanhTien: productdongia
            }),
            success: function (response) {
                console.log('Dữ liệu gửi thành công:', response);
                location.reload();
            },
            error: function (xhr, status, error) {
                console.error('Lỗi khi gửi dữ liệu:', error);
            }
        });
    } else {
        console.error('ID sản phẩm không được xác định');
    }
});

const initialProductListkh = document.getElementById('khachhang-list-container').innerHTML;

function findkhachhang() {
    let keyword = document.getElementById("searchkhachhang").value;

    if (!keyword) {
        keyword = '';
    }

    if (keyword.trim() === '') {
        document.getElementById('khachhang-list-container').innerHTML = initialProductListkh;
        return;
    }
    fetch(`http://localhost:8080/panda/banhangoffline/searchkh?keyword=${encodeURIComponent(keyword)}`)
        .then(response => response.json())
        .then(data => {
            console.log("Dữ liệu trả về từ API:", data);
            lishkhachhang(data);
        })
        .catch(error => {
            console.error('Error fetching product list:', error);
        });
}

function lishkhachhang(lskh) {
    const khachhanglist = document.getElementById('khachhang-list-container');
    if (lskh.length === 0) {
        khachhanglist.innerHTML = '<p>Không có khách hàng phù hợp </p>';
        return;
    }
    let khachhangHTML = "";
    lskh.forEach((item, index) => {
        const parts = item.split(' - ');
        const kh = {
            id: parts[0],
            makhachhang: parts[1],
            tenkhachhang: parts[2],
            sdt: parts[3],
        };
        khachhangHTML += `    
              <div class="customer-row" style="display: flex; gap: 10px; padding: 10px; border-bottom: 1px solid #ddd;">
                <div style="flex: 1; text-align: center;">${index + 1}</div>
                <div style="flex: 3; text-align: center;">${kh.makhachhang}</div>
                <div style="flex: 3; text-align: left;">${kh.tenkhachhang}</div>
                <div style="flex: 3; text-align: left;">${kh.sdt}</div>
                <div style="flex: 2; text-align: center;">
                    <a class="btn btn-dark" href="javascript:void(0);" data-id="${kh.id}" onclick="chonkh(this)">
                        Chọn
                    </a>
                </div>
            </div>
        `;

    });
    khachhanglist.innerHTML = khachhangHTML;
}

document.addEventListener("DOMContentLoaded", function () {
    const path = window.location.pathname;
    const segments = path.split("/");
    const idHoaDon = segments[segments.length - 1]; // Lấy ID hóa đơn từ URL

    const tenKhInput = document.getElementById("tenkh-input");
    const sdtInput = document.getElementById("sdt-input");
    const voucherInput = document.getElementById("mavoucher-input");

    // Sử dụng sessionStorage với khóa chứa ID hóa đơn
    const tenKhKey = `tenkh_${idHoaDon}`;
    const sdtKey = `sdt_${idHoaDon}`;
    const voucherKey = `mavoucher_${idHoaDon}`;

    // Gán giá trị đã lưu nếu tồn tại trong sessionStorage
    tenKhInput.value = sessionStorage.getItem(tenKhKey) || "";
    sdtInput.value = sessionStorage.getItem(sdtKey) || "";
    voucherInput.value = sessionStorage.getItem(voucherKey) || "";

    // Lưu dữ liệu khi người dùng nhập
    tenKhInput.addEventListener("input", function () {
        sessionStorage.setItem(tenKhKey, this.value);
    });
    sdtInput.addEventListener("input", function () {
        sessionStorage.setItem(sdtKey, this.value);
    });
    voucherInput.addEventListener("input", function () {
        sessionStorage.setItem(voucherKey, this.value);
    });

    const customerItems = document.querySelectorAll(".customer-item");

    customerItems.forEach(function (item) {
        item.addEventListener("click", function () {
            const idKhachHang = this.getAttribute("data-id");
            console.log(idKhachHang);
            fetch(`/panda/banhangoffline/chonkh?id=${idKhachHang}`)
                .then(response => response.json())
                .then(data => {
                    sessionStorage.setItem(tenKhKey, data.tenkh);
                    sessionStorage.setItem(sdtKey, data.sdt);

                    tenKhInput.value = data.tenkh;
                    sdtInput.value = data.sdt;
                })
                .catch(error => console.error("Lỗi dữ liệu", error));
        });
    });

    const voucher = document.querySelectorAll(".voucher-item");
    voucher.forEach(function (item) {
        item.addEventListener("click", function () {
            const idvoucher = this.getAttribute("data-id");
            console.log(idvoucher + "voucher");
            fetch(`/panda/banhangoffline/selectvc?id=${idvoucher}`)
                .then(response => response.json())
                .then(data => {
                    sessionStorage.setItem(voucherKey, data.mavocher);
                    voucherInput.value = data.mavocher;
                })
                .catch(error => console.error("Lỗi dữ liệu", error));
        });
    });
});




