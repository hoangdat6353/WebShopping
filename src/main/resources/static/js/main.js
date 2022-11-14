/*Bình luận và đánh giá */
var count;

function starmark(item) {
    count = item.id[0];
    sessionStorage.starRating = count;
    var subid = item.id.substring(1);
    for (var i = 0; i < 5; i++) {
        if (i < count) {
            document.getElementById((i + 1) + subid).style.color = "orange";
        } else {
            document.getElementById((i + 1) + subid).style.color = "black";
        }
    }
}

function result() {
    document.getElementById("starRating").value = count;
}

'use strict';

(function($) {

    $(' .smooth-goto').on('click', function() {
        $('html, body').animate({ scrollTop: $(this.hash).offset().top - 50 }, 1000);
        return false;
    });


    $(window).on('load', function() {
        $(".loader").fadeOut();
        $("#preloder").delay(200).fadeOut("slow");

        /*------------------
            Gallery filter
        --------------------*/
        $('.featured_controls li').on('click', function() {
            $('.featured_controls li').removeClass('active');
            $(this).addClass('active');
        });
        if ($('.featured_filter').length > 0) {
            var containerEl = document.querySelector('.featured_filter');
            var mixer = mixitup(containerEl);
        }
    });

    /*------------------
        Background Set
    --------------------*/
    $('.set-bg').each(function() {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });

    //option Menu
    $(".option_open").on('click', function() {
        $(".option_menu_wrapper").addClass("show_option_menu_wrapper");
        $(".option_menu_overlay").addClass("active");
        $("body").addClass("over_hid");
    });

    $(".option_menu_overlay").on('click', function() {
        $(".option_menu_wrapper").removeClass("show_option_menu_wrapper");
        $(".option_menu_overlay").removeClass("active");
        $("body").removeClass("over_hid");
    });

    /*------------------
		Navigation
	--------------------*/
    $(".mobile-menu").slicknav({
        prependTo: '#mobile-menu-wrap',
        allowParentLinks: true
    });

    /*-----------------------
        Categories Slider
    ------------------------*/
    $(".categories_slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 4,
        dots: false,
        nav: true,
        navText: ["<span class='fa fa-angle-left'><span/>", "<span class='fa fa-angle-right'><span/>"],
        animateOut: 'fadeOut',
        animateIn: 'fadeIn',
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true,
        responsive: {

            0: {
                items: 1,
            },

            480: {
                items: 2,
            },

            768: {
                items: 3,
            },

            992: {
                items: 4,
            }
        }
    });


    $('.brave_categories_all').on('click', function() {
        $('.brave_categories ul').slideToggle(400);
    });

    /*--------------------------
        Latest apps Slider
    ----------------------------*/
    $(".latest-apps_slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 1,
        dots: false,
        nav: true,
        navText: ["<span class='fa fa-angle-left'><span/>", "<span class='fa fa-angle-right'><span/>"],
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true
    });

    /*-----------------------------
        apps most Slider
    -------------------------------*/
    $(".apps_most_slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 3,
        dots: true,
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true,
        responsive: {

            320: {
                items: 1,
            },

            480: {
                items: 2,
            },

            768: {
                items: 2,
            },

            992: {
                items: 3,
            }
        }
    });

    /*---------------------------------
        apps Details Pic Slider
    ----------------------------------*/
    $(".apps_details_pic_slider").owlCarousel({
        loop: true,
        margin: 20,
        items: 4,
        dots: true,
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true
    });

    /*-----------------------
		Price Range Slider
	------------------------ */
    var rangeSlider = $(".price-range"),
        minamount = $("#minamount"),
        maxamount = $("#maxamount"),
        minPrice = rangeSlider.data('min'),
        maxPrice = rangeSlider.data('max');
    rangeSlider.slider({
        range: true,
        min: minPrice,
        max: maxPrice,
        values: [minPrice, maxPrice],
        slide: function(event, ui) {
            minamount.val('$' + ui.values[0]);
            maxamount.val('$' + ui.values[1]);
        }
    });
    minamount.val('$' + rangeSlider.slider("values", 0));
    maxamount.val('$' + rangeSlider.slider("values", 1));

    /*--------------------------
        Select
    ----------------------------*/
    $("select").niceSelect();

    /*------------------
		Single apps
	--------------------*/
    $('.apps_details_pic_slider img').on('click', function() {

        var imgurl = $(this).data('imgbigurl');
        var bigImg = $('.apps_details_pic_item--large').attr('src');
        if (imgurl != bigImg) {
            $('.apps_details_pic_item--large').attr({
                src: imgurl
            });
        }
    });

    /*-------------------
		Quantity change
	--------------------- */
    var proQty = $('.pro-qty');
    proQty.prepend('<span class="dec qtybtn">-</span>');
    proQty.append('<span class="inc qtybtn">+</span>');
    proQty.on('click', '.qtybtn', function() {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });

})(jQuery);



/*Subcribe bằng email ở footer*/
function Subcribe() {
    let emailFilled = document.getElementById("sub");

    let email = emailFilled.value;

    if (email === "") {
        alert('Vui lòng nhập email');
        emailFilled.focus();
        return false;
    } else if (!email.includes("@")) {
        alert('Sai định dạng email. Vui lòng nhập lại');
        emailFilled.focus();
        return false;
    } else {
        emailFilled.value = "";
        alert('Cảm ơn bạn đã quan tâm đến chúng tôi.');
        return true;
    }

}

function check_info(e) {
    if ($name == '' || $phone == '' || $address == '' || $bornDate == '') {
        e.preventDefault();
        alert("Vui lòng nhập đầy đủ thông tin người dùng trước khi nâng cấp lên nhà phát triển ");
        return false;
    } else {
        return true;
    }
}