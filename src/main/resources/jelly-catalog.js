//feather.replace();
//https://twitter.com/One_div

feather.replace();

document.querySelectorAll('.navbar__item').forEach((item, index, items) => {
    item.addEventListener('mouseenter', () => {
        items[items.length - 1].style.opacity = '1';
        items[items.length - 1].style.transform = `translateY(${index * 3.5}rem)`;
    });
    item.addEventListener('mouseleave', () => {
        items[items.length - 1].style.opacity = '0';
    });
});