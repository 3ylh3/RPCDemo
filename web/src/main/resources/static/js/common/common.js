/**
 * 加载menu
 */
function loadMenu() {
    var data = [
        {
            text: "应用列表",
            selectable: false,
            href: "application_list.html"
        },
        {
            text: "服务列表",
            selectable: false,
            href: "service_list.html"
        }
    ];
    var menu = {
        data: data,
        color: "#428bca",
        searchResultColor: "#FFFFFF"
    }
    $('#menu').treeview(menu);
    //加载iframe为第一项菜单
    var result = $('#menu').treeview('getNode', 0);
    $("#iframe").attr("src", result.href);
}

/**
 * 修改bootstrap-treeview.min.js源码实现点击菜单调用该方法重新加载iframe
 * @param href
 */
function loadIfream(href) {
    $("#iframe").attr("src", href);
}