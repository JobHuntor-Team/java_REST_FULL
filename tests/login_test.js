Feature('Login API Automation');

Scenario('SCRUM-52, 54, 55: Comprehensive Login Testing', async ({ I }) => {
    const loginUrl = 'http://localhost:8080/api/v1/auth/login';

    // 1. [SCRUM-52] Negative Test: Tài khoản không tồn tại
    await I.sendPostRequest(loginUrl, {
        username: "admin@gmail.com",
        password: "password123"
    });
    I.seeResponseCodeIs(400);
    I.say('✅ PASSED: Server chặn tài khoản không tồn tại');

    // 2. [SCRUM-52] Test Biên: Mật khẩu để trống
    await I.sendPostRequest(loginUrl, {
        username: "admin@gmail.com",
        password: ""
    });
    I.seeResponseCodeIs(400);
    I.say('✅ PASSED: Server chặn mật khẩu trống');

    // 3. [SCRUM-54] Security Test: SQL Injection
    // Chỉnh thành 400 vì Server chặn ở tầng Validation trước khi vào Auth
    await I.sendPostRequest(loginUrl, {
        username: "' OR '1'='1' --",
        password: "any_password"
    });
    I.seeResponseCodeIs(400);
    I.say('✅ PASSED: Server nhận diện và chặn chuỗi SQL độc hại');

    // 4. [SCRUM-55] Stress Test: Payload cực lớn (1MB)
    const massivePassword = "a".repeat(1024 * 1024);
    await I.sendPostRequest(loginUrl, {
        username: "hiep_stress@gmail.com",
        password: massivePassword
    });
    I.seeResponseCodeIs(400);
    I.say('✅ PASSED: Server xử lý an toàn dữ liệu rác 1MB');
});