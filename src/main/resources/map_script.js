console.log('初始化地图开始');

// 初始化地图
var map = new AMap.Map('map', {
    center: [116.407395, 39.904211], // 北京的默认中心点
    zoom: 10 // 缩放级别
});

console.log('高德地图对象创建成功');

// 地址解析函数
function geocodeAddress(address, callback) {
    const geocodeUrl = `https://restapi.amap.com/v3/geocode/geo?address=${address}&output=json&key=9e5e537082455e2b444b7841da988ac7`;

    fetch(geocodeUrl)
        .then(response => response.json())
        .then(data => {
            if (data.status === '1' && data.geocodes.length > 0) {
                const location = data.geocodes[0].location; // 获取经纬度
                console.log(`地址解析成功: ${address}, 坐标: ${location}`);
                callback(location); // 调用回调函数并传递坐标
            } else {
                console.error('地址解析失败:', data);
            }
        })
        .catch(error => {
            console.error('请求地址解析失败:', error);
        });
}

// 添加标记点函数，使用不同的图标
function addMarker(location, title, iconUrl) {
    const [lng, lat] = location.split(',').map(parseFloat);

    // 自定义标记点图标
    var icon = new AMap.Icon({
        size: new AMap.Size(24, 34), // 图标大小
        image: iconUrl, // 自定义图标
        imageSize: new AMap.Size(24, 34) // 图标图片大小
    });

    var marker = new AMap.Marker({
        position: [lng, lat],
        title: title,
        map: map,
        icon: icon, // 使用自定义图标
        offset: new AMap.Pixel(-12, -34), // 图标偏移量，确保图标底部与坐标对齐
        zooms: [10, 20], // 缩放级别
        cursor: 'pointer'
    });

    // 鼠标悬停时放大效果
    marker.on('mouseover', function() {
        marker.setAnimation('AMAP_ANIMATION_BOUNCE'); // 跳动效果
    });

    marker.on('mouseout', function() {
        marker.setAnimation(''); // 停止动画
    });

    // 点击时显示地址信息
    marker.on('click', function() {
        var infoWindow = new AMap.InfoWindow({
            content: `<strong>${title}</strong>`,
            offset: new AMap.Pixel(0, -30),
            position: [lng, lat]
        });
        infoWindow.open(map, marker.getPosition());
    });
}

// 定义从 Java 传递的起点和终点文字地址，并进行地理编码
window.displayRoute = function(startAddress, endAddress) {
    console.log('显示路线从 ' + startAddress + ' 到 ' + endAddress);

    // 先将起点和终点地址解析为经纬度坐标
    geocodeAddress(startAddress, function(startLocation) {
        geocodeAddress(endAddress, function(endLocation) {
            // 坐标格式为 "经度,纬度"，需要直接用于路线规划 API
            const directionsUrl = `https://restapi.amap.com/v3/direction/driving?origin=${startLocation}&destination=${endLocation}&strategy=0&extensions=all&output=json&key=9e5e537082455e2b444b7841da988ac7`;

            fetch(directionsUrl)
                .then(response => response.json())
                .then(data => {
                    if (data.status === '1') {
                        console.log('路线规划成功', data);

                        const path = data.route.paths[0].steps.map(step => step.polyline.split(';').map(coord => {
                            return coord.split(',').map(parseFloat);
                        })).flat(); // 将所有 polyline 分割为二维数组

                        // 创建路线
                        const routeLine = new AMap.Polyline({
                            path: path, // 路线坐标
                            borderWeight: 4, // 线宽
                            strokeColor: '#FF0000', // 线颜色
                            lineJoin: 'round', // 折线拐点圆角处理
                            strokeWidth: 5, // 线宽度
                            strokeOpacity: 1, // 线透明度
                        });

                        // 将路线添加到地图上
                        map.add(routeLine);

                        // 调整地图视野，使路线全部展示在视野内
                        map.setFitView([routeLine]);

                        // 添加起点和终点的标记点，使用相对路径
                        addMarker(startLocation, '起点: ' + startAddress, './flag1.png');
                        addMarker(endLocation, '终点: ' + endAddress, './flag2.png');

                    } else {
                        console.error('路线规划失败:', data);
                    }
                })
                .catch(error => {
                    console.error('请求驾车路径规划失败:', error);
                });
        });
    });
};

// 地图加载完成后的事件
map.on('complete', function () {
    console.log('地图加载成功');
});
