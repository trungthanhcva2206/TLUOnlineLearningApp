# TLUOnlineLearningApp

Repo này chứa mã nguồn cho Ứng dụng học trực tuyến TLULearning, phục vụ cho môn học Phát triển ứng dụng trên các thiết bị di động. Ứng dụng được phát triển bằng Java và nhắm mục tiêu đến các thiết bị Android với API SDK tối thiểu là 26.

## Tổng quan

Ứng dụng TLULearning nhằm mục đích lấp đầy khoảng trống trong trải nghiệm học tập trên thiết bị di động cho sinh viên và giảng viên tại Trường Đại học Thủy Lợi, cung cấp một nền tảng chuyên biệt, thân thiện với người dùng và được tối ưu hóa cho thiết bị di động. Ứng dụng tập trung vào việc mang lại trải nghiệm học tập liền mạch và hiệu quả, tích hợp chặt chẽ với chương trình đào tạo của nhà trường

# Nhóm sinh viên thực hiện dự án

- [Nguyen Thi Phuong Anh](https://github.com/ntpa812) - 2251161942 - 64HTTT.NB
- [Tran Hung Anh](https://github.com/hunganh7204) - 2251061712 - 64CNTT.NB
- [Nguyen Le Trung Thanh](https://github.com/trungthanhcva2206) - 2251061885 - 64CNTT.NB

## NOTE

- [Tài liệu báo cáo](https://docs.google.com/document/d/1a4ASaS-tqENg4HVe3Yz0cIjeiJf_au0h/edit?usp=sharing&ouid=103250931260588488904&rtpof=true&sd=true)
- [Thiết kế giao diện](https://www.figma.com/design/x5MHCLc5jwGazjYQ0upEeW/TLULearning---mobile?m=auto&t=5z1FBiagNUEK4qwh-1)

## Tính năng

Ứng dụng phục vụ ba đối tượng người dùng chính: Sinh viên, Giảng viên và Quản trị viên

### Tính năng của Sinh viên:
* **Xem thông tin cá nhân**: Sinh viên có thể đăng nhập và xem thông tin tài khoản cá nhân của mình
* **Theo dõi khóa học**: Xem danh sách các môn học đang tham gia
* **Truy cập nội dung**: Truy cập các bài giảng và tài liệu
* **Tìm kiếm khóa học**: Tìm kiếm các khóa học cụ thể bằng cách nhập tên khóa học hoặc bộ môn
* **Lịch sử học tập**: Xem lại lịch sử các khóa học đã tham gia, bao gồm thông tin chi tiết và tiến độ

### Tính năng của Giảng viên:
* **Quản lý khóa học**: Tạo mới và quản lý các khóa học (thêm, sửa, ẩn/hiện bài giảng, đăng tải tài liệu)
* **Tải lên nội dung**: Tải lên các video bài giảng và tài liệu bổ sung cho các bài học
* **Ẩn/hiện khóa học**: Thay đổi trạng thái hiển thị của một khóa học (ẩn hoặc hiện) đối với sinh viên
* **Xem các khóa học đã tạo**: Xem danh sách tất cả các khóa học mà mình đã tạo trong hệ thống

### Tính năng của Quản trị viên:
* **Quản lý tài khoản người dùng**: Cấp phát và quản lý tài khoản cho sinh viên và giảng viên (tạo tài khoản mới, xem danh sách tài khoản, cập nhật thông tin tài khoản, khóa tài khoản)

### Tính năng chung (Đăng nhập và Chat):
* **Đăng nhập**: Người dùng có thể đăng nhập vào hệ thống bằng tài khoản của mình để sử dụng các chức năng tương ứng với quyền hạn của họ
* **Trò chuyện nhóm**: Tham gia trao đổi thông tin chung trong môi trường trò chuyện nhóm theo thời gian thực

## Công nghệ sử dụng

* **Nền tảng**: Android
* **Ngôn ngữ**: Java
* **Hệ thống xây dựng**: Gradle
* **SDK tối thiểu**: API 26
* **SDK mục tiêu**: API 35

## Bắt đầu

### Điều kiện tiên quyết

* Android Studio
* Java Development Kit (JDK) 11 trở lên
* Gradle (URL phân phối được chỉ định trong `gradle/wrapper/gradle-wrapper.properties`)

### Cài đặt

1.  **Sao chép kho lưu trữ**
    ```bash
    git clone [https://github.com/trungthanhcva2206/tluonlinelearningapp.git](https://github.com/trungthanhcva2206/tluonlinelearningapp.git)
    ```
2.  **Mở trong Android Studio**
    Mở dự án đã sao chép trong Android Studio
3.  **Đồng bộ Gradle**
    Android Studio sẽ tự động đồng bộ dự án với các tệp Gradle. Nếu không, hãy đồng bộ thủ công bằng cách nhấp vào "Sync Project with Gradle Files" trên thanh công cụ
4.  **Chạy trên thiết bị hoặc trình giả lập**
    Chọn một thiết bị hoặc trình giả lập mục tiêu và chạy ứng dụng

## Thiết kế cơ sở dữ liệu

Cơ sở dữ liệu của ứng dụng được thiết kế dựa trên mô hình quan hệ, được chuẩn hóa thành dạng chuẩn BCNF (Boyce-Codd Normal Form) để giảm thiểu sự dư thừa dữ liệu và các bất thường khi cập nhật

**Các thực thể và thuộc tính chính**:
* `users`: `user_id` (PK), `username`, `password`, `role`, `avatar_url`, `full_name`, `status`, `created_at`, `updated_at`
* `departments`: `department_id` (PK), `department_name`
* `courses`: `course_id` (PK), `course_name`, `description`, `thumbnail_url`, `teacher_id` (FK đến `users`), `department_id` (FK đến `departments`), `status`, `created_at`, `updated_at`
* `lessons`: `lesson_id` (PK), `course_id` (FK đến `courses`), `lesson_title`, `description`, `order_index`, `created_at`, `updated_at`
* `videos`: `video_id` (PK), `lesson_id` (FK đến `lessons`, ràng buộc duy nhất), `video_title`, `video_url`, `duration`, `created_at`, `updated_at`
* `documents`: `document_id` (PK), `lesson_id` (FK đến `lessons`), `document_title`, `file_url`, `file_type`, `created_at`, `updated_at`
* `course_registrations`: `regis_id` (PK), `user_id` (FK đến `users`), `course_id` (FK đến `courses`), `progress`, `created_at`, `updated_at`
* `messages`: `message_id` (PK), `sender_id` (FK đến `users`), `content`, `created_at`
* `notifications`: `notif_id` (PK), `user_id` (FK đến `users`), `notif_type`, `content`, `status`, `created_at`

## Hướng phát triển trong tương lai

Các kế hoạch phát triển trong tương lai cho ứng dụng TLULearning bao gồm:
* **Tương tác nâng cao**: Triển khai các tính năng như bài kiểm tra, nộp bài tập
* **Cải thiện trải nghiệm người dùng**: Tập trung vào cá nhân hóa và khả năng học ngoại tuyến
* **Tích hợp công nghệ mới**: Khám phá gamification (trò chơi hóa) và AI để tạo ra một hệ sinh thái học tập kỹ thuật số toàn diện
