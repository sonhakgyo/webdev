// package tonic_project;

// public class Prod {
    
// }
// public boolean insertProduct(HttpServletRequest request) {
//     boolean b = false;
//     try {
//         String uploadDir = "C:/Users/kitcoop/git/espriter_blog_practice_kic/espriter_blog_bbs/WebContent/data";
//         // 업로드할 이미지 경로(절대 경로)
//         // MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, new DefaultFileRenamePolicy());
//         MultipartRequest multi = new MultipartRequest(request, uploadDir, 5*1024*1024, "utf-8", new DefaultFileRenamePolicy());
//         conn = ds.getConnection();
//         String sql = "insert into shop_product(name, price, detail, sdate, stock, image) values(?,?,?,now(),?,?)"; //now는 sdate(지금) 의미
//         pstmt = conn.prepareStatement(sql);
//         pstmt.setString(1,  multi.getParameter("name"));
//         pstmt.setString(2,  multi.getParameter("price"));
//         pstmt.setString(3,  multi.getParameter("detail"));
//         // sdate는 입력 안해도 자체적으로 생성됨
//             pstmt.setString(4,  multi.getParameter("stock"));
//             if(multi.getFilesystemName("image") == null) {
//                     // 신상품 이미지를 택하지 않았을 경우 디폴트 이미지 선택
//                     pstmt.setString(5, "../images/ready.gif");
//             }else {
//                     pstmt.setString(5,  multi.getFilesystemName("image"));
//             }            
//             return result == 1;
//     } catch(Exception e) {
//             System.out.println("MemberDAO isExistID(): "+ e +" 에러");
//             e.printStackTrace();
//     } finally {
//             JDBCUtil.disconnect(pstmt, con);    
//     }
//     return b;
// }
