$(document).ready(function () {/* jQuery toggle layout */
    $('#btnToggle').click(function () {
        if ($(this).hasClass('on')) {
            $('#main .col-md-6').addClass('col-md-4').removeClass('col-md-6');
            $(this).removeClass('on');
        }
        else {
            $('#main .col-md-4').addClass('col-md-6').removeClass('col-md-4');
            $(this).addClass('on');
        }
    });
});
// 답변추가
$(".submit-write button").click(addAnswer);

function addAnswer(e) {
    e.preventDefault();
    var queryString = $("form.submit-write").serialize(); // form data 들을 자동으로 묶어준다.
    console.log("queryString : {}", queryString);
    $.ajax({
        type: 'post',
        url: '/api/qna/addAnswer',
        data: queryString,
        dataType: 'json',
        error: onError,
        success: onSuccess
    });
}

function onSuccess(json, status) {
    // 서버로부터 전달받은 데이터를 json 형태로 잘 왔는지 확인
    console.log(json);
    // answerTemplate을 읽어온다.
    var answerTemplate = $("#answerTemplate").html();
    var template = answerTemplate.format(json.answer.writer, new Date(json.answer.createdDate), json.answer.contents, json.answer.answerId);
    // 해당 클래스 뒤에 template 파일을 붙인다.
    $(".qna-comment-slipp-articles").prepend(template);
    $(".submit-write textarea").val("");

}

function onError() {
    console.log("error");
}

String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};
// event delegation
$(".qna-comment-slipp-articles").on("click", ".delete-answer button", deleteAnswer);

function deleteAnswer(e) {
    e.preventDefault();

    var queryString = $("form.delete-answer").serialize();

    console.log('qs', queryString);
    console.log(e);
    $.ajax({
        type: 'post',
        url: '/api/qna/deleteAnswer',
        data: queryString,
        dataType: 'json',
        error: onError,
        success: function (json, status) {
            if (json.result.status) {
                e.currentTarget.closest('article').remove();
            }
        }
    });
}

