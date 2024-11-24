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
    updateShippingFee();
});
$("#district").change(() => {
    callApiWard(host + "d/" + $("#district").val() + "?depth=2");
    updateAddressResult();


});
$("#ward").change(() => {
    updateAddressResult();
});

var updateAddressResult = () => {
    let provinceText = $("#province option:selected").text() || "Tỉnh/Thành phố";
    let districtText = $("#district option:selected").text() || "Quận/Huyện";
    let wardText = $("#ward option:selected").text() || "Xã/Phường";

    let result = `${provinceText} | ${districtText} | ${wardText}`;
    $("#result").text(result);

    $("#selectedProvince").val(provinceText !== "Tỉnh/Thành phố" ? provinceText : "");
    $("#selectedDistrict").val(districtText !== "Quận/Huyện" ? districtText : "");
    $("#selectedWard").val(wardText !== "Xã/Phường" ? wardText : "");
};

var updateShippingFee = () => {
    let province = $("#province option:selected");
    let region = province.data("region");
    let provinceName = province.text();

    if (provinceName === "Thành phố Hà Nội" || provinceName ===  "Chọn") {
        $("#phiship").val(0);
        $("#ship").text("0 VND");
    } else {
        $("#phiship").val(30000);
        $("#ship").text("30,000.00");
    }
};

