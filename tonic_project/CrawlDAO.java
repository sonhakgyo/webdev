package tonic_project;
// package model.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

// cos.jar 파일 가져오기 <-- 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// import model.common.JDBCUtil; // 이클립스
import tonic_project.JDBCUtil;   // VSC

public class CrawlDAO {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public static void insertCrawlDB() {
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql_InsertVITA = "INSERT INTO product(pno, pcode, pimg_src, pbrand, pname, pprice, pdiscount, porigin, pperiod, psales, pstock) " + 
				"VALUES(LPAD(PROD_SEQ.NEXTVAL, 3, 0), 'VITA',"
				+ "?,"
				+ "DECODE(ROUND(DBMS_RANDOM.VALUE(1, 5)), 1, '네이처메이드', 2, '네추럴라이즈', 3, '한미헬스케어', 4, '어피어', 5, '라이프익스텐션'),"
				+ "?,"
				+ "?,"
				+ "'0%'," + 
				"DECODE(ROUND(DBMS_RANDOM.VALUE(1, 7)),1, '국내산', 2, '중국산', 3, '미국산', 4, '대만산', 5, '일본산', 6, '독일산', 7, '파푸아뉴기니산')," + 
				"DECODE(ROUND(DBMS_RANDOM.VALUE(1, 7)),1,'2022년 4월',2,'2022년 9월',3,'2022년 12월',4,'2023년 5월',5,'2023년 7월',6,'2023년 11월',7,'이미 썩었음')," + 
				"ROUND(DBMS_RANDOM.VALUE(1, 200)), ROUND(DBMS_RANDOM.VALUE(1, 200)))";
		String sql_InsertLACT = "INSERT INTO product(pno, pcode, pimg_src, pbrand, pname, pprice, pdiscount, porigin, pperiod, psales, pstock) " + 
				"VALUES(LPAD(PROD_SEQ.NEXTVAL, 3, 0), 'LACT', "
				+ "?, "
				+ "DECODE(ROUND(DBMS_RANDOM.VALUE(1, 5)), 1, '엔젯오리진', 2, '닥터브라이언', 3, '라플레', 4, '어피어', 5, '라이프익스텐션'), "
				+ "?, "
				+ "?, "
				+ "'0%'," + 
				"DECODE(ROUND(DBMS_RANDOM.VALUE(1, 7)),1, '국내산', 2, '중국산', 3, '미국산', 4, '대만산', 5, '일본산', 6, '독일산', 7, '파푸아뉴기니산')," + 
				"DECODE(ROUND(DBMS_RANDOM.VALUE(1, 7)),1,'2022년 4월',2,'2022년 9월',3,'2022년 12월',4,'2023년 5월',5,'2023년 7월',6,'2023년 11월',7,'이미 썩었음')," + 
				"ROUND(DBMS_RANDOM.VALUE(1, 200)), ROUND(DBMS_RANDOM.VALUE(1, 200)))";
		String sql_InsertEYES = "INSERT INTO product(pno, pcode, pimg_src, pbrand, pname, pprice, pdiscount, porigin, pperiod, psales, pstock) " + 
				"VALUES(LPAD(PROD_SEQ.NEXTVAL, 3, 0), 'EYES', "
				+ "?, "
				+ "DECODE(ROUND(DBMS_RANDOM.VALUE(1, 5)), 1, '닥터브라이언', 2, '네추럴라이즈', 3, '한미헬스케어', 4, '어피어', 5, '라이프익스텐션'), "
				+ "?, "
				+ "?, "
				+ "'0%'," + 
				"DECODE(ROUND(DBMS_RANDOM.VALUE(1, 7)),1, '국내산', 2, '중국산', 3, '미국산', 4, '대만산', 5, '일본산', 6, '독일산', 7, '파푸아뉴기니산')," + 
				"DECODE(ROUND(DBMS_RANDOM.VALUE(1, 7)),1,'2022년 4월',2,'2022년 9월',3,'2022년 12월',4,'2023년 5월',5,'2023년 7월',6,'2023년 11월',7,'이미 썩었음')," + 
				"ROUND(DBMS_RANDOM.VALUE(1, 200)), ROUND(DBMS_RANDOM.VALUE(1, 200)))";
		// 상품별로 순차적으로 url을 수정한다. --> String 배열에 넣어 for문 작성
		String url1 = "https://www.uppear.co.kr/goods/goods_list.php?page=1&cateCd=003005&sort=&pageNum=40"; // 종합비타민
		String url2 = "https://www.uppear.co.kr/goods/goods_list.php?cateCd=003001"; // 유산균
		String url3 = "https://www.uppear.co.kr/goods/goods_list.php?cateCd=003015"; // 눈건강
		// --> String 배열에 넣어 for문 작성
		String sql_Insert[] = {sql_InsertVITA, sql_InsertLACT, sql_InsertEYES}; 
		String url[] = {url1, url2, url3};
		
		int result = 0;
		con = JDBCUtil.connect();
		try {
			for (int i = 0; i < 3; i++) {
				Document doc = Jsoup.connect(url[i]).get(); // url의 HTML 코드를 가져온다.
				Elements ele = doc.select("div.goods_list_cont"); // 그 중 div의 item_cont 클래스 태그로 둘러쌓인 부분을 가져온다. 
				Iterator<Element> itr_img_src = ele.select("div.item_photo_box > a > img").iterator(); // 정상
				Iterator<Element> itr_name = ele.select("strong.item_name").iterator(); // 정상
				Iterator<Element> itr_price = ele.select("strong.item_price").iterator(); // 정상
				
				while(itr_name.hasNext()) {
					pstmt = con.prepareStatement(sql_Insert[i]); // [sql_InsertVITA, sql_InsertLACT, sql_InsertEYES]
					pstmt.setString(1, itr_img_src.next().attr("abs:src"));
					pstmt.setString(2, itr_name.next().text());
					pstmt.setInt(3, Integer.parseInt(itr_price.next().text().replaceAll("[,원]", ""))); // 이건 대박이다.
					result += pstmt.executeUpdate();
				}
			}
			/* for문 하나로 줄이기
			// 종합비타민 크롤링 후 db에 넣기
			Document doc1 = Jsoup.connect(url1).get(); // url의 HTML 코드를 가져온다.
			Elements ele1 = doc1.select("div.goods_list_cont"); // 그 중 div의 item_cont 클래스 태그로 둘러쌓인 부분을 가져온다. 
			Iterator<Element> itr_img_src1 = ele1.select("div.item_photo_box > a > img").iterator(); // 정상
			Iterator<Element> itr_name1 = ele1.select("strong.item_name").iterator(); // 정상
			Iterator<Element> itr_price1 = ele1.select("strong.item_price").iterator(); // 정상
			
			while(itr_name1.hasNext()) {
			//  상품별로 순차적으로 sql문을 수정한다.
				pstmt = con.prepareStatement(sql_InsertVITA); // sql_InsertVITA, sql_InsertLACT, sql_InsertEYES
				pstmt.setString(1, itr_img_src1.next().attr("abs:src"));
				pstmt.setString(2, itr_name1.next().text());
				pstmt.setString(3, itr_price1.next().text());
//				System.out.println("이미지 : " + itr_img_src.next().attr("abs:src")); // src 상대 경로? | abs:src = src의 절대 경로
//				System.out.println("상품명 : " + itr_name.next().text()); 
//				System.out.println("가격 : " + itr_price.next().text());
				result += pstmt.executeUpdate();
			}
			
			// 유산균 크롤링 후 db에 넣기
			Document doc2 = Jsoup.connect(url2).get(); 
			Elements ele2 = doc1.select("div.goods_list_cont");  
			Iterator<Element> itr_img_src2 = ele2.select("div.item_photo_box > a > img").iterator(); 
			Iterator<Element> itr_name2 = ele2.select("strong.item_name").iterator(); 
			Iterator<Element> itr_price2 = ele2.select("strong.item_price").iterator(); 
			while(itr_name2.hasNext()) {
				pstmt = con.prepareStatement(sql_InsertLACT); 
				pstmt.setString(1, itr_img_src2.next().attr("abs:src"));
				pstmt.setString(2, itr_name2.next().text());
				pstmt.setString(3, itr_price2.next().text());
				result += pstmt.executeUpdate();
			}
			// 눈건강(=루테인) 크롤링 후 db에 넣기
			Document doc3 = Jsoup.connect(url3).get(); 
			Elements ele3 = doc3.select("div.goods_list_cont");  
			Iterator<Element> itr_img_src3 = ele3.select("div.item_photo_box > a > img").iterator(); 
			Iterator<Element> itr_name3 = ele3.select("strong.item_name").iterator(); 
			Iterator<Element> itr_price3 = ele3.select("strong.item_price").iterator(); 
			while(itr_name3.hasNext()) {
				pstmt = con.prepareStatement(sql_InsertEYES);
				pstmt.setString(1, itr_img_src3.next().attr("abs:src"));
				pstmt.setString(2, itr_name3.next().text());
				pstmt.setString(3, itr_price3.next().text());
				result += pstmt.executeUpdate();
			} */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("크롤링 데이터 수 : " + result);
			JDBCUtil.disconnect(pstmt, con); 
		}
	}
}
