$(document).ready(function (){
    $("#learnMore").click(function (){
        $(this).blur();
        $('html, body').animate({
            scrollTop: $("#motivation").offset().top - 150
        }, 700);
    });

    $("#showCode").click(function (){
        $(this).blur();
        $('html, body').animate({
            scrollTop: $("#code").offset().top - 80
        }, 700);
    });

    $("#showFeatures").click(function (){
        $(this).blur();
        $('html, body').animate({
            scrollTop: $("#features").offset().top - 80
        }, 700);
    });
});