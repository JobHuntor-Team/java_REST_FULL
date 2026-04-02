exports.config = {
  tests: './tests/*_test.js',
  output: './output',
  helpers: {
    // Tạm thời tắt Playwright để tập trung test Backend
    REST: {
      endpoint: 'http://localhost:8080', // Kiểm tra xem port của Hiệp có đúng 8080 không?
      onRequest: (request) => {
        request.headers.auth = 'secret';
      }
    },
    JSONResponse: {}
  },
  include: {
    I: './steps_file.js'
  },
  name: 'java_REST_FULL'
}

// test