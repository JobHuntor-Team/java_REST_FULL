Feature('Login API');

Scenario('Test Login với các giá trị biên (Backend Only)', async ({ I }) => {
    const loginUrl = 'http://localhost:8080/api/v1/auth/login';

    // 1. Test Login thành công
    const res = await I.sendPostRequest(loginUrl, {
        username: "admin@gmail.com",
        password: "password123"
    });

    // Thay vì dùng I.seeResponseCodeIs, mình dùng lệnh assertion trực tiếp:
    I.seeResponseCodeIs(200);

    // 2. Test mật khẩu trống
    await I.sendPostRequest(loginUrl, {
        username: "admin@gmail.com",
        password: ""
    });
    I.seeResponseCodeIs(400);
});