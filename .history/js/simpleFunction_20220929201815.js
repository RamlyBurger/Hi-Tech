// Functions For header
function logoHover() {
    document.getElementById("logo").setAttribute('src', 'icon/logo-highlighted.png');
}
function logoUnhover() {
    document.getElementById("logo").setAttribute('src', 'icon/logo.png');
}
function logo2Hover() {
    document.getElementById("logo2").setAttribute('src', 'icon/logo2-highlighted.png');
}
function logo2Unhover() {
    document.getElementById("logo2").setAttribute('src', 'icon/logo2.png');
}
function whyushover() {
    document.getElementById("lightIco").setAttribute('src', 'icon/Lightning-highlighted.png');
}
function whyusunhover() {
    document.getElementById("lightIco").setAttribute('src', 'icon/Lightning.png');
}
function whyushover2() {
    document.getElementById("awardIco").setAttribute('src', 'icon/Membership-highlighted.png');
}
function whyusunhover2() {
    document.getElementById("awardIco").setAttribute('src', 'icon/Membership.png');
}
function whyushover3() {
    document.getElementById("serviceIco").setAttribute('src', 'icon/Service-highlighted.png');
}
function whyusunhover3() {
    document.getElementById("serviceIco").setAttribute('src', 'icon/Services.png');
}
function changeStyle(){
        var element = document.getElementById('slide1');
        element.style.opacity = 0;
}
function searchBar(){
    search = document.getElementById("searchContent").value;
    alert("Searching for \"" + search + "\"");
    alert("Searching in progress...");
}