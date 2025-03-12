USE EduSys;
go

--STORED PROCEDURE 
--GET ALL STAFFS --> STAFFS TABLE
CREATE OR ALTER PROC sp_getAllStaffs
AS
BEGIN
	SELECT * FROM DBO.NHANVIEN;
END
GO

--INSERT INTO STAFFS --> STAFFS TABLE (?-set nocount on)
CREATE OR ALTER PROC sp_mergeStaffs
    @StaffsJson NVARCHAR(MAX),
    @deleteMaNV NVARCHAR(50) = NULL  -- Thêm tham số để xác định nhân viên cần xóa
AS
BEGIN
    SET NOCOUNT ON;

    -- Table variable để lưu danh sách nhân viên cập nhật/thêm mới
    CREATE TABLE #TempStaffs (
        MaNV NVARCHAR(50),
        MatKhau NVARCHAR(50),
        HoTen NVARCHAR(100),
        VaiTro BIT
    );

    -- Đưa dữ liệu JSON vào bảng tạm
    INSERT INTO #TempStaffs (MaNV, MatKhau, HoTen, VaiTro)
    SELECT 
        JSON_VALUE(value, '$.code') AS MaNV,
        JSON_VALUE(value, '$.password') AS MatKhau,
        JSON_VALUE(value, '$.name') AS HoTen,
        CASE 
            WHEN JSON_VALUE(value, '$.role') = 'Quan ly' THEN 1
            WHEN JSON_VALUE(value, '$.role') = 'Nhan vien' THEN 0
            ELSE 0
        END AS VaiTro
    FROM OPENJSON(@StaffsJson);

    -- Sử dụng MERGE để thực hiện UPSERT
    MERGE DBO.NHANVIEN AS Target
    USING #TempStaffs AS Source
    ON Target.MaNV = Source.MaNV
    WHEN MATCHED THEN
        -- Cập nhật các bản ghi đã tồn tại
        UPDATE SET 
            Target.MatKhau = Source.MatKhau,
            Target.HoTen = Source.HoTen,
            Target.VaiTro = Source.VaiTro
    WHEN NOT MATCHED THEN
        -- Thêm mới các bản ghi không tồn tại
        INSERT (MaNV, MatKhau, HoTen, VaiTro)
        VALUES (Source.MaNV, Source.MatKhau, Source.HoTen, Source.VaiTro);

    -- Xóa nhân viên nếu tham số @deleteMaNV có giá trị
    IF @deleteMaNV IS NOT NULL
    BEGIN
        DELETE FROM DBO.NHANVIEN WHERE MaNV = @deleteMaNV;
    END
END
GO

--DUNG DE ON TAP
CREATE OR ALTER PROC sp_mergeStaffs1
    @StaffsJson NVARCHAR(MAX),
    @deleteMaNV NVARCHAR(50) = NULL  -- Tham số để xác định nhân viên cần xóa
AS
BEGIN
    SET NOCOUNT ON;

    -- Tạo bảng tạm để lưu danh sách nhân viên cập nhật/thêm mới
    CREATE TABLE #TempStaffs (
        MaNV NVARCHAR(50),
        MatKhau NVARCHAR(50),
        HoTen NVARCHAR(100),
        VaiTro BIT,
        GioiTinh NVARCHAR(10)  -- ✅ Đã sửa lỗi dấu phẩy
    );

    -- Đưa dữ liệu JSON vào bảng tạm
    INSERT INTO #TempStaffs (MaNV, MatKhau, HoTen, VaiTro, GioiTinh)
    SELECT 
        JSON_VALUE(value, '$.code') AS MaNV,
        JSON_VALUE(value, '$.password') AS MatKhau,
        JSON_VALUE(value, '$.name') AS HoTen,
        CASE 
            WHEN JSON_VALUE(value, '$.role') = 'Quan ly' THEN 1
            WHEN JSON_VALUE(value, '$.role') = 'Nhan vien' THEN 0
            ELSE 0
        END AS VaiTro,
        JSON_VALUE(value, '$.gender') AS GioiTinh  

    FROM OPENJSON(@StaffsJson);

    -- Sử dụng MERGE để thực hiện UPSERT
    MERGE DBO.NHANVIEN AS Target
    USING #TempStaffs AS Source
    ON Target.MaNV = Source.MaNV
    WHEN MATCHED THEN
        -- Cập nhật các bản ghi đã tồn tại
        UPDATE SET 
            Target.MatKhau = Source.MatKhau,
            Target.HoTen = Source.HoTen,
            Target.VaiTro = Source.VaiTro,
            Target.GioiTinh = Source.GioiTinh
    WHEN NOT MATCHED THEN
        -- Thêm mới các bản ghi không tồn tại
        INSERT (MaNV, MatKhau, HoTen, VaiTro, GioiTinh)
        VALUES (Source.MaNV, Source.MatKhau, Source.HoTen, Source.VaiTro, Source.GioiTinh);

    -- Xóa nhân viên nếu tham số @deleteMaNV có giá trị
    IF @deleteMaNV IS NOT NULL
    BEGIN
        DELETE FROM DBO.NHANVIEN WHERE MaNV = @deleteMaNV;
    END
END
GO



--STORED --> STUDENTS TABLE
--getAllStudents

CREATE OR ALTER PROC sp_getAllStudents
AS
BEGIN
	SELECT * FROM NguoiHoc
END
GO
exec dbo.sp_getAllStudents


--mergeStudents
CREATE OR ALTER PROC sp_mergeStudents
	@StudentsJson NVARCHAR(MAX)
AS
BEGIN
	SET NOCOUNT ON;

	CREATE TABLE #TempStudents(
		MaNH NCHAR(7),
		HoTen NVARCHAR(50),
		NgaySinh DATE,
		GioiTinh BIT,
		DienThoai NVARCHAR(50),
		Email NVARCHAR(50),
		GhiChu NVARCHAR(50),
		MaNV NVARCHAR(50),
		NgayDK DATE
	);


	-- Đưa dữ liệu JSON vào bảng tạm
	INSERT INTO #TempStudents (MaNH, HoTen, NgaySinh, GioiTinh, DienThoai, Email, GhiChu, MaNV, NgayDK)
	SELECT
		JSON_VALUE(value, '$.stuCode') as MaNH,
		JSON_VALUE(value, '$.name') as HoTen,
		JSON_VALUE(value, '$.date') as NgaySinh,
		CASE
			WHEN JSON_VALUE(value, '$.gender') = 'Nam' THEN 1
			WHEN JSON_VALUE(value, '$.gender') = 'Nu' THEN 0
			ELSE 0
		END AS GioiTinh,
		JSON_VALUE(value, '$.phoneNum') as DienThoai,
		JSON_VALUE(value, '$.email') as Email,
		JSON_VALUE(value, '$.description') as GhiChu,
		JSON_VALUE(value, '$.staffCode') as MaNV,
		JSON_VALUE(value, '$.registerDay') as NgayDK
	FROM OPENJSON(@StudentsJson)

	-- Sử dụng MERGE để thực hiện UPSERT
	MERGE DBO.NguoiHoc AS TARGET
	USING #TempStudents AS SOURCE
	ON TARGET.MaNH = SOURCE.MaNH
	WHEN MATCHED THEN
	UPDATE SET
		TARGET.MaNH = SOURCE.MaNH,
		TARGET.HoTen = SOURCE.HoTen,
		TARGET.NgaySinh = SOURCE.NgaySinh,
		TARGET.GioiTinh = SOURCE.GioiTinh,
		TARGET.DienThoai = SOURCE.DienThoai,
		TARGET.Email = SOURCE.Email,
		TARGET.GhiChu = SOURCE.GhiChu,
		TARGET.MaNV = SOURCE.MaNV,
		TARGET.NgayDK = SOURCE.NgayDK
	WHEN NOT MATCHED THEN
		INSERT (MaNH, HoTen, NgaySinh, GioiTinh, DienThoai, Email, GhiChu, MaNV, NgayDK)
		VALUES (SOURCE.MaNH, SOURCE.HoTen, SOURCE.NgaySinh, SOURCE.GioiTinh, SOURCE.DienThoai, SOURCE.Email, SOURCE.GhiChu, SOURCE.MaNV, SOURCE.NgayDK);

	DROP TABLE #TempStudents;
END
GO

--STORED -> THEMATIC STUDY
--getAllChuyenDe
CREATE OR ALTER PROC sp_getAllChuyenDe
AS
BEGIN
	SELECT * FROM ChuyenDe
END
GO

--mergeThematicStudy
CREATE OR ALTER PROC sp_mergeThematicStudy
    @ThematicJson NVARCHAR(MAX)
AS
BEGIN
    --SET NOCOUNT ON;

    -- Khai báo Table Variable (TỐI ƯU hơn bảng tạm)
    DECLARE @TempThematic TABLE (
        MaCD NCHAR(5),
        TenCD NVARCHAR(50),
        HocPhi FLOAT,
        ThoiLuong INT,
        Hinh NVARCHAR(50),
        MoTa NVARCHAR(255)
    );

    -- Đưa dữ liệu JSON vào Table Variable
    INSERT INTO @TempThematic (MaCD, TenCD, HocPhi, ThoiLuong, Hinh, MoTa)
    SELECT
        JSON_VALUE(value, '$.code') AS MaCD,
        JSON_VALUE(value, '$.name') AS TenCD,
        JSON_VALUE(value, '$.tuitionFee') AS HocPhi,
        JSON_VALUE(value, '$.time') AS ThoiLuong,
        JSON_VALUE(value, '$.picture') AS Hinh,
        JSON_VALUE(value, '$.describe') AS MoTa
    FROM OPENJSON(@ThematicJson);

    -- Debug: Kiểm tra dữ liệu JSON đã được parse đúng chưa
    PRINT 'Dữ liệu JSON đã được chuyển thành bảng:';
    SELECT * FROM @TempThematic;

    -- Thực hiện MERGE (UPSERT)
    MERGE DBO.ChuyenDe AS TARGET
    USING @TempThematic AS SOURCE
    ON TARGET.MaCD = SOURCE.MaCD
    WHEN MATCHED THEN
        UPDATE SET
            TARGET.TenCD = SOURCE.TenCD,
            TARGET.HocPhi = SOURCE.HocPhi,
            TARGET.ThoiLuong = SOURCE.ThoiLuong,
            TARGET.Hinh = SOURCE.Hinh,
            TARGET.MoTa = SOURCE.MoTa
    WHEN NOT MATCHED THEN
        INSERT (MaCD, TenCD, HocPhi, ThoiLuong, Hinh, MoTa)
        VALUES (SOURCE.MaCD, SOURCE.TenCD, SOURCE.HocPhi, SOURCE.ThoiLuong, SOURCE.Hinh, SOURCE.MoTa);

    PRINT 'MERGE hoàn thành!';
END
GO



--STORED -> COURSE_MANAGEMENT

--Fill ComboBox
CREATE OR ALTER PROC sp_getCDCourse
AS
BEGIN
    SELECT DISTINCT CD.TenCD
    FROM ChuyenDe CD
	INNER JOIN KhoaHoc KH ON KH.MaCD = CD.MaCD
END
GO

--Fill vao Table
CREATE OR ALTER PROC sp_getCourseByTenCD
    @TenCD NVARCHAR(100)
AS
BEGIN
    DECLARE @MaCD NCHAR(5)

    -- Lấy MaCD từ bảng ChuyenDe
    SELECT @MaCD = MaCD FROM ChuyenDe WHERE TenCD = @TenCD

    -- Nếu không tìm thấy MaCD, vẫn phải trả về một ResultSet trống
    IF @MaCD IS NULL
    BEGIN
        SELECT NULL AS MaKH, NULL AS TenKH, NULL AS HocPhi, NULL AS ThoiGian -- Giữ định dạng bảng
        RETURN
    END

    -- Trả về danh sách khóa học (nếu có)
    SELECT * FROM KhoaHoc WHERE MaCD = @MaCD
END
GO

--Kiem MaCD dua vao TenCD
CREATE OR ALTER PROCEDURE sp_getMaCD
    @TenCD NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    -- Trả về MaCD ngay cả khi không có khóa học nào trong KhoaHoc
    SELECT DISTINCT CD.MaCD
    FROM ChuyenDe CD
    LEFT JOIN KhoaHoc KH ON CD.MaCD = KH.MaCD
    WHERE LTRIM(RTRIM(CD.TenCD)) COLLATE SQL_Latin1_General_CP1_CI_AI 
          = LTRIM(RTRIM(@TenCD)) COLLATE SQL_Latin1_General_CP1_CI_AI;
END;
GO

--mergeCourse
	CREATE OR ALTER PROC sp_mergeCourse
    @Action NVARCHAR(10),  -- 'ADD', 'UPDATE', 'DELETE'
    @MaKH INT = NULL,      -- Mã Khóa Học (NULL nếu thêm mới)
    @MaCD NVARCHAR(10) = NULL,  -- Mã Chuyên Đề
    @HocPhi FLOAT = NULL,       -- Học Phí
    @ThoiLuong INT = NULL,      -- Thời Lượng (giờ)
    @MaNV NVARCHAR(50) = NULL,  -- Người Tạo
    @GhiChu NVARCHAR(MAX) = NULL,  -- Ghi Chú
    @NgayKG DATE = NULL,        -- Ngày Khai Giảng
    @NgayTao DATE = NULL        -- Ngày Tạo
AS
BEGIN
    -- Thêm khóa học mới (MaKH tự động tạo)
    IF @Action = 'ADD'
    BEGIN
        INSERT INTO KhoaHoc (MaCD, HocPhi, ThoiLuong, MaNV, GhiChu, NgayKG, NgayTao)
        VALUES (@MaCD, @HocPhi, @ThoiLuong, @MaNV, @GhiChu, @NgayKG, @NgayTao);

        -- Lấy ID mới tạo ra
        SELECT SCOPE_IDENTITY() AS MaKH;
    END

    -- Cập nhật khóa học (chỉ khi MaKH tồn tại)
    ELSE IF @Action = 'UPDATE'
    BEGIN
        UPDATE KhoaHoc
        SET MaCD = @MaCD,
            HocPhi = @HocPhi,
            ThoiLuong = @ThoiLuong,
            MaNV = @MaNV,
            GhiChu = @GhiChu,
            NgayKG = @NgayKG,
            NgayTao = @NgayTao
        WHERE MaKH = @MaKH;
    END

    -- Xóa khóa học
    ELSE IF @Action = 'DELETE'
    BEGIN
        DELETE FROM KhoaHoc WHERE MaKH = @MaKH;
    END
END
GO


--STORED -> COURSEMEMBER_MANAGEMENT
--getAllCourseMember
CREATE OR ALTER PROC sp_getAllCourseMember
AS
BEGIN
	SELECT * FROM HocVien
END
GO

--getMaCDNgayKG
CREATE OR ALTER PROC sp_getMaCDNgayKG
	@TenCD NVARCHAR(50)
AS
BEGIN
	SELECT KH.MaCD, KH.NgayKG 
	FROM KhoaHoc KH
	JOIN ChuyenDe CD ON CD.MaCD = KH.MaCD
	WHERE LTRIM(RTRIM(CD.TenCD)) COLLATE SQL_Latin1_General_CP1_CI_AI 
          = LTRIM(RTRIM(@TenCD)) COLLATE SQL_Latin1_General_CP1_CI_AI;
END
GO

--getMaKH
CREATE OR ALTER PROC sp_getMaKH
	@MaCD NCHAR(5),
	@NgayKG DATE
AS
BEGIN
	SELECT MaKH FROM KhoaHoc
	WHERE MaCD = @MaCD
	AND NgayKG = @NgayKG
END
GO

--mergeCourseMember
CREATE OR ALTER PROC sp_getProcCourseMember
	@MaKH INT
AS
BEGIN
	SELECT HV.MaHV, HV.MaNH, NH.HoTen, HV.Diem
	FROM HocVien HV
	JOIN NguoiHoc NH ON NH.MaNH = HV.MaNH
	WHERE HV.MaKH = @MaKH
END
GO


--STORED -> STATICTICAL_SUMMARY

--getProcTranscript
--FILL ComboBox
CREATE OR ALTER PROC sp_getCDTranscript
AS
BEGIN
	SELECT MaCD, NgayKG 
	FROM dbo.KhoaHoc
END
GO


--Get MaKH
CREATE OR ALTER PROC sp_getMaKH
	@MaCD nchar(5)
AS
BEGIN
	SELECT MaKH FROM dbo.KhoaHoc
	WHERE MaCD = @MaCD
END
GO

--getProcGradeSummary
CREATE OR ALTER PROC sp_getProcGradeSummary
AS
BEGIN
	SELECT * FROM [dbo].[sp_DiemChuyenDe]
END
GO



--getProcRevenue
--Fill Combobox
CREATE OR ALTER PROC sp_getYearRevenue
AS
BEGIN
	SELECT DISTINCT Year(NgayKG) as "year" FROM KhoaHoc
END
GO

CREATE OR ALTER PROC sp_getProcRevenue
AS
BEGIN
	SELECT * FROM [dbo].[sp_DoanhThu]
END
GO
















