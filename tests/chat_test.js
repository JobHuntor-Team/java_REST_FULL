Feature('Kiểm tra Requirement Module Chat');

Scenario('REQ-01: Kiểm tra giao diện danh sách chat', ({ I }) => {
    I.amOnPage('/messenger');
    I.see('Messenger', 'h3'); // Kiểm tra tiêu đề trang
    I.seeElement('.list-message-items'); // Kiểm tra có danh sách người chat
});

Scenario('REQ-02: Kiểm tra luồng gửi tin nhắn thành công', ({ I }) => {
    I.amOnPage('/messenger');
    I.fillField('input[placeholder="Write a message..."]', 'Test tin nhắn tự động');
    I.pressKey('Enter');
    I.see('Test tin nhắn tự động', '.message-content-wrapper'); // Yêu cầu mong muốn: Tin nhắn phải hiển thị
});

// Check Jira connection