$(document).ready(function (){
    $("#learnMore").click(function (){
        $(this).blur();
        $('html, body').animate({
            scrollTop: $("#features").offset().top - 150
        }, 700);
    });
});