console.log('初始化PDF.js开始');

// PDF.js配置
var pdfjsLib = window['pdfjs-dist/build/pdf'];
pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.worker.min.js';

var pdfDoc = null;
var scale = 1.5;
var container = document.getElementById('pdf-viewer');

// 加载PDF文件
function loadPdf(url) {
    console.log('加载PDF文件: ' + url);
    var loadingTask = pdfjsLib.getDocument(url);
    loadingTask.promise.then(function(pdf) {
        console.log('PDF加载成功');
        pdfDoc = pdf;
        var numPages = Math.min(pdf.numPages, 20); // 只渲染前20页
        console.log('总页数: ' + pdf.numPages + ', 渲染页数: ' + numPages);

        // 清空之前的内容
        container.innerHTML = '';

        // 循环渲染每一页
        for (var pageNum = 1; pageNum <= numPages; pageNum++) {
            renderPage(pageNum);
        }
    }, function(reason) {
        console.error('PDF加载失败: ' + reason);
    });
}

// 渲染单页
function renderPage(pageNum) {
    pdfDoc.getPage(pageNum).then(function(page) {
        console.log('获取第' + pageNum + '页成功');

        var viewport = page.getViewport({ scale: scale });

        // 创建canvas元素
        var canvas = document.createElement('canvas');
        var context = canvas.getContext('2d');
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        // 设置Canvas元素样式
        canvas.style.display = 'block';
        canvas.style.margin = '0 auto';

        // 渲染PDF页面
        var renderContext = {
            canvasContext: context,
            viewport: viewport
        };
        page.render(renderContext).promise.then(function() {
            console.log('第' + pageNum + '页渲染完成');
        });

        // 将canvas添加到容器中
        container.appendChild(canvas);
        console.log('第' + pageNum + '页已添加到容器中');
    });
}

// 从Java调用的函数，用于加载PDF文件
window.displayPdf = function(pdfUrl) {
    console.log('显示PDF: ' + pdfUrl);
    loadPdf(pdfUrl);
};

// 初始化时加载一个示例PDF
window.onload = function() {
    loadPdf('http://localhost:8082/files/sample.pdf');
};
