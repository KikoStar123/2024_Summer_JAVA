function createFireflies(quantity) {
    for (var i = 0; i < quantity; i++) {
        var firefly = document.createElement('div');
        firefly.className = 'firefly';
        var size = Math.random() * 0.5 + 0.2 + 'vw';
        firefly.style.width = size;
        firefly.style.height = size;
        firefly.style.left = Math.random() * 100 + 'vw';
        firefly.style.top = Math.random() * 100 + 'vh';
        firefly.style.animation = 'drift ' + (Math.random() * 10 + 5) + 's infinite linear, flash ' + (Math.random() * 3 + 1) + 's infinite linear';
        //firefly.style.animation = 'drift ' + (Math.random() * 10 + 5) + 's infinite linear, flash ' + (Math.random() * 6 + 1) + 's infinite linear';
        document.body.appendChild(firefly);
    }
}

createFireflies(30);

// 每3秒移动一次萤火虫
//setInterval(moveFireflies, 2000);