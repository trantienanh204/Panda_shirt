
const host = "https://provinces.open-api.vn/api/";
var callAPI = (api) => {
    return axios.get(api)
        .then((response) => {
            renderData(response.data, "province");
        });
}
callAPI('https://provinces.open-api.vn/api/?depth=1');
var callApiDistrict = (api) => {
    return axios.get(api)
        .then((response) => {
            renderData(response.data.districts, "district");
        });
}
var callApiWard = (api) => {
    return axios.get(api)
        .then((response) => {
            renderData(response.data.wards, "ward");
        });
}

var renderData = (array, select) => {
    let row = ' <option disable value="">Chọn</option>';
    array.forEach(element => {
        row += `<option value="${element.code}">${element.name}</option>`
    });
    document.querySelector("#" + select).innerHTML = row
}

$("#province").change(() => {
    $("#ward").html('<option value="">Chọn</option>');
    callApiDistrict(host + "p/" + $("#province").val() + "?depth=2");
    updateAddressResult();
});
$("#district").change(() => {
    callApiWard(host + "d/" + $("#district").val() + "?depth=2");
    updateAddressResult();


});
$("#ward").change(() => {
    updateAddressResult();
});

var updateAddressResult = () => {
    let provinceId = $("#province").val() || "";
    let districtText = $("#district").val() || "";
    let wardText = $("#ward").val() || "";

    let result = `${provinceId} | ${districtText} | ${wardText}`;
    $("#result").text(result);

    $("#selectedProvince").val(provinceId);
    $("#selectedDistrict").val(districtText);
    $("#selectedWard").val(wardText);
};
$(document).ready(() => {
    const savedCustomer = JSON.parse(localStorage.getItem("selectedCustomer"));
    if (savedCustomer) {
        const {idTinhThanhPho, idQuanHuyen, idXaPhuong} = savedCustomer;
        console.log("Dữ liệu từ localStorage:", savedCustomer);

        // Gọi lại API để tải danh sách tỉnh thành
        callAPI('https://provinces.open-api.vn/api/?depth=1').then(() => {
            $("#province").val(idTinhThanhPho).trigger("change");


            callApiDistrict(`https://provinces.open-api.vn/api/p/${idTinhThanhPho}?depth=2`).then(() => {
                $("#district").val(idQuanHuyen).trigger("change");

                callApiWard(`https://provinces.open-api.vn/api/d/${idQuanHuyen}?depth=2`).then(() => {
                    $("#ward").val(idXaPhuong).trigger("change");
                });
            });
        });

        $("#selectedProvince").val(idTinhThanhPho);
        $("#selectedDistrict").val(idQuanHuyen);
        $("#selectedWard").val(idXaPhuong);
    }
});

