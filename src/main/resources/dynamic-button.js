document.addEventListener('DOMContentLoaded', function() {
    // Get the button element by its ID
    var button = document.getElementById('dynamicButton');

    // Get the text from the data-text attribute
    var buttonText = button.getAttribute('data-text');

    // Set the text content of the button
    button.innerHTML = '<span>' + buttonText + '</span>';

    // Add a click event listener to the button
    button.addEventListener('click', function() {
        alert('Button clicked!');
    });
});
