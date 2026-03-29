Feature('Nhiệm vụ Job - JobHunter');

Scenario('Kiểm tra xem danh sách công việc và chi tiết job', ({ I }) => {
    // 1. Vào trang danh sách Job
    I.amOnPage('/jobs');

    // 2. Kiểm tra xem có danh sách job hiện ra không
    I.waitForElement('.job-card', 5);
    I.say('Đã thấy danh sách các công việc');

    // 3. Click vào một Job cụ thể để xem chi tiết
    I.click('.job-card:first-child');

    // 4. Kiểm tra xem có hiện nút "Apply" hoặc nút "Chat" trong Job đó không
    I.see('Apply Now');
    I.see('Chat with HR');
});