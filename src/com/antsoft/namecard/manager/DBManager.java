package com.antsoft.namecard.manager;

import com.antsoft.namecard.*;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 *  DBManager
 *
 *  This class manages DB connection.
 */
class DBManager {
	/** connection information */
	static NameCardSite site  = new NameCardSite();
	static Uni2Asc uni = new Uni2Asc();
	static Asc2Uni asc = new Asc2Uni();
	
	static String PATH = "/home/httpd/html/cardSystem/logo/";

	/**
	 *  constructor
	 */
	public DBManager() {
	}

	///////////////////////////  Card  //////////////////////////////////

	/**
	 *	getCardContent - 카드에 보여줄 내용을 DB에서 쿼리한다.
	 *
	 *	@param card_id 		쿼리할 사용자의 아이디
	 *	@param person_kind 	쿼리할 사용자의 종류( momber or nonmember )
	 *
	 */
	public ResultSet getCardContent( String card_id, String person_kind ) throws CommandException{

		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
			throw new CommandException( e.toString() );
		}

		try{
			String query1 = null;
			String query2 = null;

			//query statement
			query1 = "select MemberPerson.name, MemberPerson.description," +
					 " PersonInfo.handphone, PersonInfo.email, PersonInfo.homepage," +
					" PersonInfo.beeper, JobInfo.posi, JobInfo.part, JobInfo.tel," +
					" Company.name, ComInfo.address, Company.site, PersonInfo.tel, JobInfo.fax, " +
					" MemberPerson.mybg, MemberPerson.myfont,  MemberPerson.mylogo, " +
					" MemberPerson.cardbg, MemberPerson.cardfont, MemberPerson.cardlogo " +
					" from MemberPerson, PersonInfo, JobInfo, Company, ComInfo" +
					" where MemberPerson.id ='" + card_id + 
					"' and MemberPerson.personinfo =PersonInfo.id" +
					" and MemberPerson.jobinfo =JobInfo.id" +
					" and MemberPerson.com_id =Company.id" +
					" and Company.cominfo =ComInfo.id;"; 

			query2 = "select NonMemberPerson.name," +
					" PersonInfo.handphone, PersonInfo.email, PersonInfo.homepage," +
					" PersonInfo.beeper, JobInfo.posi, JobInfo.part, JobInfo.tel," +
					" Company.site, Company.name, PersonInfo.address, JobInfo.fax, " + 
					" PersonInfo.tel, JobInfo.kind, Company.region " +
					" from NonMemberPerson, PersonInfo, JobInfo, Company " +
					" where NonMemberPerson.id ='" + card_id +
					"' and NonMemberPerson.personinfo =PersonInfo.id" +
					" and NonMemberPerson.jobinfo =JobInfo.id" +
					" and NonMemberPerson.com_id=Company.id; "; 

			//query 문 실행
			if( person_kind.equals( "m" ) ) result = stmt.executeQuery( query1 );
			else result = stmt.executeQuery( query2 );

			stmt.close();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to getCardContent at DBManager " + e.toString() );
		}

		return result;
	}	
				
	/**
	 *	getCardListContent - 카드리스트에 보여줄 내용을 DB에서 쿼리한다.
	 *
	 *	@param card_id 쿼리할 사용자의 아이디
	 *	@param person_kind 	쿼리할 사용자의 종류( momber or nonmember )
	 *
	 */
	public ResultSet getCardListContent( String card_id, String person_kind ) throws CommandException{

		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
			throw new CommandException( e.toString() );
		}

		try{
			String query1 = null;
			String query2 = null;

			//query statement
			query1 = "select MemberPerson.name, JobInfo.posi, Company.name" +
					" from MemberPerson, JobInfo, Company" +
					" where MemberPerson.id='" + card_id + 
					"'and JobInfo.id=MemberPerson.jobinfo" +
					" and Company.id=MemberPerson.com_id order by 1;"; 

			query2 = "select NonMemberPerson.name, JobInfo.posi, Company.name" +
					" from NonMemberPerson, JobInfo, Company" +
					" where NonMemberPerson.id='" + card_id +
					"' and JobInfo.id=NonMemberPerson.jobinfo" +
					" and Company.id=NonMemberPerson.com_id order by 1;"; 

			//query 문 실행
			if( person_kind.equals( "m" ) ) result = stmt.executeQuery( query1 );
			if( person_kind.equals( "n" ) ) result = stmt.executeQuery( query2 );

			stmt.close();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to getCardContent at DBManager " + e.toString() );
		}

		return result;
	}

	/**
	 *	existCard - 디렉토리에 이미 명함이 등록되어 있는지를 알려준다
	 * 
	 *  @param id       
	 *	@param name 	쿼리할 사용자의 이름
	 *	@param c_name 	쿼리할 사용자의 회사이름
	 *	@param c_region 쿼리할 사용자의 회사 지역
	 *
	 */
	public String existCard( String id, String name, String c_name, int c_region ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result = null;

		String re = null;

		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			String query = null;
			
			//query statement
			query = "select name from Directory " +  
					" where Directory.owner ='" + id + 
					"' and NonMemberPerson.name ='" + name + 
					"' and Card.card_id =NonMemberPerson.id" +
					" and Directory.id =Card.dir_id" +
					" and Company.name ='" + c_name + 
					"' and Company.region ='" + c_region + 
 					"' and NonMemberPerson.com_id = Company.id;"; 

			//query 문 실행
			result = stmt.executeQuery( query );

			if( result.next()) re = result.getString( "id" );

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to getCardContent at DBManager " + e.toString() );
		}

		return re;
	}

	/**
	 *	existCard - 디렉토리에 이미 명함이 등록되어 있는지를 알려준다
	 *
	 *	@param card_id 	쿼리할 카드 아이디 
	 *	@param id 		명함주인 
	 *
	 */
	public String existCard( String card_id, String id ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result = null;

		String re = null;

		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			String query = null;
			
			//query statement
			query = "select name from Directory" +  
					" where Directory.owner ='" + id + "'" + 
					" and Card.card_id ='" + card_id + "'" +
					" and Directory.id =Card.dir_id;"; 
					

			//query 문 실행
			result = stmt.executeQuery( query );

			if( result.next() ) re = result.getString( "name" );

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to getCardContent at DBManager " + e.toString() );
		}

		return re;
	}				

	/**
	 *	searchNonmem_MyCard - 내 디렉토리에 있는 멤버의 카드를 검색한다.
	 *
	 *	@param id 		사용자의 아이디
	 *	@param name 	쿼리할 사용자의 이름
	 *	@param c_name 	쿼리할 사용자의 회사이름
	 *	@param c_region 쿼리할 사용자의 회사 지역
	 *	@param c_site 	쿼리할 사용자의 회사 지점
	 *	@param kind 	쿼리할 사용자의 직업종류
	 *
	 */
	public ResultSet searchNonmem_MyCard( String id, String name, String c_name, String c_region, String c_site, String kind  ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result = null;

		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
						
			/*String query = "select distinct NonMemberPerson.id, NonMemberPerson.name, Company.name, JobInfo.part," +
					" JobInfo.posi, Directory.id, Card.person_kind" +
					" from NonMemberPerson, Company, JobInfo, Directory, Card" +  
					" where Directory.owner ='" + id + 
					"' and Card.dir_id=Directory.id" +
					" and NonMemberPerson.id=Card.card_id";*/ 
			String query = "select NonMemberPerson.id,  " +
					"  Directory.id, Card.person_kind, NonMemberPerson.name " +
					" from NonMemberPerson, Directory, Card" +  
					" where Directory.owner ='" + id + 
					"' and Card.dir_id=Directory.id" +
					" and NonMemberPerson.id=Card.card_id"; 
			String q1 = " and NonMemberPerson.name ='" + name + "'"; 
			String q2 =	" and JobInfo.id=NonMemberPerson.jobinfo and JobInfo.kind='" + Integer.parseInt( kind ) + "'" ;
			String q3 = " and Company.name LIKE '%" + c_name + "%'";
			String q4 =	" and Company.site ='" + c_site + "'"; 
			String q5 = " and Company.region ='" + Integer.parseInt( c_region ) + "'";
 			String q6 = " and Company.id=NonMemberPerson.com_id"; 

			//query statement
			if( !name.equals( "" )) query += q1;
			if( !kind.equals( "0" ) ) query += q2;
			if( !c_name.equals( "" )) query = query + q6 + q3;
			if( !c_site.equals( "") ) query = query + q6 + q4;
			if( !c_region.equals( "0" ) ) query = query + q6 + q5;

			query += " order by name;";

			//query 문 실행
			result = stmt.executeQuery( query );

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to searchNonmem_MyCard at DBManager " + e.toString() );
		}

		return result;
	}

	/**
	 *	searchMem_MyCard - 내 디렉토리에 있는 넌멤버의 카드를 검색한다.
	 *
	 *	@param id 		사용자의 아이디
	 *	@param name 	쿼리할 사용자의 이름
	 *	@param c_name 	쿼리할 사용자의 회사이름
	 *	@param c_region 쿼리할 사용자의 회사 지역
	 *	@param c_site 	쿼리할 사용자의 회사 지점
	 *	@param kind 	쿼리할 사용자의 직업종류
	 *
	 */
	public ResultSet searchMem_MyCard( String id, String name, String c_name, String c_site, String c_region, String kind  ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result = null;

		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{

			String query = "select MemberPerson.id, " +
					"  Directory.id, MemberPerson.name " +
					" from MemberPerson, Directory " +  
					" where Directory.owner = '" + id  + "'" + 
					" and Card.dir_id=Directory.id" +
					" and MemberPerson.id=Card.card_id"; 
			String q1 = " and MemberPerson.name ='" + name + "'"; 
			String q2 =	" and MemberPerson.jobinfo=JobInfo.id and JobInfo.kind = '" + Integer.parseInt( kind )  + "'";
			String q3 = " and Company.name LIKE '%" + c_name + "%'";
			String q4 =	" and Company.site='" + c_site + "'"; 
			String q5 = " and Company.region=" + Integer.parseInt( c_region );
 			String q6 = " and MemberPerson.com_id=Company.id"; 

			//query statement
			if( !name.equals( "" ) ) query += q1;
			if( !kind.equals( "0" ) ) query += q2;
			if( !c_name.equals( "" ) ) query = query + q6 + q3;
			if( !c_site.equals( "" ) ) query = query + q6 + q4;
			if( !c_region.equals( "0" ) ) query = query + q6 + q5;

			query += " order by name ;";

			//query 문 실행
			result = stmt.executeQuery( query );

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query execute in searchMem_myCard" + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to searchMem_MyCard at DBManager " + e.toString() );
		}

		return result;
	}

	/**
	 *	searchPublicCard
	 *
	 *	@param name 	검색할 이름
	 *	@param c_name 	검색할 회사이름
	 *	@param c_region	검색할 회사의 지역
	 *	@param c_site	검색할 회사의 지점
	 *	@param kind		검색할 회사의 업종
	 *
	 */
	public ResultSet searchPublicCard( String name, String c_name, String c_region, String c_site, String kind ) throws CommandException{

	
		Connection con;
		Statement stmt;
		ResultSet result;


		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}
String query = null;
		try{
			//파라메터에 따라 검색한다.
			query = "select MemberPerson.id " + 
							" from MemberPerson where";   
			String q1 = " MemberPerson.name ='" + name + "'"; 
			String q2 =	" JobInfo.id=MemberPerson.jobinfo and JobInfo.kind=" + Integer.parseInt( kind ) ;
			String q3 = " Company.name LIKE '%" + c_name + "%'";
			String q4 =	" Company.site='" + c_site + "'"; 
			String q5 = " Company.region=" + Integer.parseInt( c_region );
 			String q6 = " MemberPerson.com_id =Company.id "; 

			boolean flag = false;

			//query statement
			if( !name.equals("") ) {
				query = query + q1;
				flag = true;
			}
			if( !kind.equals( "0" ) ) {
				if( flag ) query = query + " and " + q2;
				else query = query + q2;
				flag = true;
			}
			if( !c_name.equals("")) {
				if( flag ) query = query + " and " + q3 + " and "  +q6;
				else query = query + q3 + " and " + q6;
				flag = true;
			}
			if( !c_site.equals( "" ) ){
				if( flag ) query = query + " and " + q4 + " and " +  q6;
				else query = query + q4 + " and " + q6;
				flag = true;
			}
			if( !c_region.equals( "0" ) ) {
				if( flag ) query = query + " and " + q5 + " and " + q6;
				else query = query + q5 + " and " + q6;
				flag = true;
			}
			query += ";";

			//query문 실행
			result = stmt.executeQuery( query );

			//DB close
			stmt.close();
			con.close();

		}catch( Exception e ){
			throw new CommandException( "fail to searchPublicCard at DBManager"  + query+ e.toString() );
		}

		return result;
	}
	/**
	 *	getCardList - 카드 리스트를 얻어 리턴한다.
	 *
	 *	@param dir_id	카드가 있는 디렉토리 아이디
	 *
	 */	
	public ResultSet getCardList( String dir_id ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			//query statement
			String query = " select * from Card where dir_id='" + dir_id + "';";

			//query 문 실행
			result = stmt.executeQuery( query );
			stmt.close();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to getCardList at DBManager " + e.toString() );
		}

		return result;
	}

	/**
	 *	getPersonKind - 명함이 멤버의 것인지 넌멤버의 것인지를 리턴한다. 
	 *
	 *	@param dir_id	카드가 있는 디렉토리 아이디
	 *	@param card_id	카드 아이디
	 *
	 */	
	public ResultSet getPersonKind( String dir_id, String card_id ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			String query = null;

			if( dir_id == null ){
				//query statement
				query = " select person_kind from Card where card_id = '" + card_id + "';";
			}else{
				query = " select person_kind from Card where dir_id ='" + dir_id + "' and card_id = '" + card_id + "';";
			}

			//query 문 실행
			result = stmt.executeQuery( query );
			stmt.close();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query execute " + e.toString() );
		}catch( Exception e ){
			throw new CommandException ( "fail to getPersonKind at DBManager " + e.toString() );
		}

		return result;
	}

	/**
	 *	addCard - 새 카드를 DB에 추가한다.
	 *
	 *	@param dir_id		카드가 있는 디렉토리 아이디
	 *	@param card_id		카드 아이디
	 *	@param person_kind	member인지 non member인지 알려주는 변수
	 *
	 */
	public void addCard( String card_id, String dir_id, String person_kind ) throws CommandException{

		Connection con;
		Statement stmt;
		String message;
		
		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			//query statement
			String query1 = "insert into Card (dir_id, card_id, person_kind) values ('" + dir_id + "', '" + card_id + "', '" + person_kind + "' );";

			//query문 실행
			stmt.executeUpdate( query1 );

			// 등록된 명함수를 얻는다.
			String query2 = "select number from Directory where id='" + dir_id + "';";
		 	ResultSet result = stmt.executeQuery( query2 );
			result.next();
		 	int number = result.getInt( "number" );
		 	number = number + 1;

			String query3 = "update Directory set number='" + number + "' where id='" + dir_id + "';";
			stmt.executeUpdate( query3 );

			con.commit();
			stmt.close();
			con.close();
			return;
		}catch( SQLException e ){
			message = "fail to insert new card in query" ;
		}catch( Exception e ){
			message = "fail to insert new card in DBManager";
		}

		try {
			con.rollback();
			con.close();
		} catch( SQLException e ) {
			throw new CommandException( message + e.toString() ); 
		}
	}

	/**
	 *	deleteCard - 카드를 DB에서 삭제한다.
	 *
	 *	@param card_id 카드 아이디
	 *
	 */
	public void deleteCard( String card_id, String dir_id ) throws CommandException{

		Connection con;
		Statement stmt;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );

		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}
		String message = null;

		try{
			//query statement
			String query1 = "delete from Card where dir_id = '" + dir_id + "' and card_id = '" + card_id + "';"; 
			String query2 = "delete from PersonInfo where NonMemberPerson.id='" + card_id + "' and" + 
							" PersonInfo.id=NonMemberPerson.personinfo;";
			String query3 = "delete from JobInfo where NonMemberPerson.id='" + card_id + "' and" +
							" JobInfo.id=NonMemberPerson.jobinfo;";

			message = "in query1";
			stmt.executeUpdate( query1 );
			message = "in query2";
			stmt.executeUpdate( query2 );
			message = "in query3";
			stmt.executeUpdate( query3 );

			// 등록된 명함수를 얻는다.
			message = "in query5";
			String query5 = "select number from Directory where id='" + dir_id + "';";
		 	ResultSet result = stmt.executeQuery( query5 );
			result.next();
		 	int number = result.getInt( "number" );
		 	number = number - 1;

			message = "in query6";
			String query6 = "update Directory set number='" + number + "' where id='" + dir_id + "';";

			//query문 실행
			
			stmt.executeUpdate( query6 );

			message = "in query7";
			String query7 = "select employee, cominfo from Company " + 
							" where NonMemberPerson.id='" + card_id + "'" + 
							" and Company.id=NonMemberPerson.com_id;";  
			ResultSet com = stmt.executeQuery( query7 );
			int employee = 0;

			if( com.next()) {
				employee = com.getInt( "employee" );
			}

			// 직원이 1명뿐이면 회사도 삭제한다.
			if( employee == 1 ) {
				ResultSet cominfo = null;
				String query8 = null;
				String query9 = null;
 
				// connection에서 statement를 생성해서 query한다.
				String back = com.getString( "cominfo" );
			
				// cominfo가 있는 경우만 지운다.
				if( !back.equals( "none" ) ) {
					while( !back.equals( "0" ) ) {			
						message = "in query8";
						query8 = "select back from ComInfo where id='" + back + "';";
						
						// com을 아이디로 하는 cominfo의 back을 얻는다. 
						cominfo = stmt.executeQuery( query8 );
						cominfo.next();

						// cominfo를 삭제한다.	
						message = "in query9";
						query9 = "delete from ComInfo where id='" + back + "';";
						stmt.executeUpdate( query9 ); 
						back = cominfo.getString( "back" ); 

					}	
				}
				// Company를 삭제한다.
				message = "in query10";
				String query10 = "delete from Company where NonMemberPerson.id='" + card_id + "'" +
									" and Company.id=NonMemberPerson.com_id ;";
				stmt.executeUpdate( query10 );

			} else { 
				// company에서 직원수를 얻어와서 1 감소시킨다.
				employee = employee - 1;
				message="in query11";
				String query11 = "update Company set employee='" + employee + "'" +  
							" where NonMemberPerson.id='" + card_id + "'" + 
							" and Company.id=NonMemberPerson.com_id;"; 
				// 회사에 등록된 직원이 한명도 없는 경우
				stmt.executeUpdate( query11 ); 	
			} // end of if-else

			message = "in query4";
			String query4 = "delete from NonMemberPerson where id='" + card_id + "';";
			stmt.executeUpdate( query4 );

			con.commit();
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to delete execute " + message + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to deleteCard at DBManager " + message + e.toString() );
		}
	}

	/**
	 *	updateCard - 카드를 수정한다.
	 *
	 *	@param card_id 		수정할 카드 아이디
	 *	@param dir_id 		카드가 들어있는 디렉토리 아이디
	 *	@param person_kind 	person type
	 *	@param to 			수정할 데이터( 옮길 디렉토리 아이디)	
	 *
	 */
	public void updateCard( String card_id, String dir_id, String to ) throws CommandException{

		Connection con;
		Statement stmt;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			con.setAutoCommit( false );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			//query statement
			String query1 = "update Card set  dir_id ='" + to + "' where dir_id = '" + dir_id + "'and card_id = '" + card_id + "';";

			//query 문 실행
			stmt.executeUpdate( query1 );
			
			// 원래 있던 디렉토리의 명함수를 감소시킨다. 
			String query2 = "select number from Directory where id='" + dir_id + "';";
		 	ResultSet result = stmt.executeQuery( query2 );
			result.next();
		 	int number = result.getInt( "number" );
		 	number = number - 1;

			String query3 = "update Directory set number='" + number + "' where id='" + dir_id + "';";

			//query문 실행
			stmt.executeUpdate( query3 );

			// 옮겨지는 디렉토리의 명함수를 증가시킨다. 
			String query4 = "select number from Directory where id='" + to + "';";
		 	result = stmt.executeQuery( query4 );
			result.next();
		 	number = result.getInt( "number" );
		 	number = number + 1;

			String query5 = "update Directory set number='" + number + "' where id='" + to + "';";

			//query문 실행
			stmt.executeUpdate( query5 );

			con.commit();
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to update execute " + e.toString() );
		}catch( Exception  e ){
			throw new CommandException( "fail to updateCard at DBManager " + e.toString() );
		}
	}


	///////////////////////////////  Color  /////////////////////////////////	

	/**
	 *  insertColor - color를 DB에 저장한다.
	 */
	public void insertColor() throws CommandException {
		Connection con;
		Statement stmt;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			con.setAutoCommit( false );
			stmt = con.createStatement();			
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}
	String query = "";	
		try {

			query = "insert into Color (id, color) values('1', 'f0f8ff');"; 
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('2', 'faebd7' );";  
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('3', '00ffff' );";  
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('4', '7fffd4' );";  
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('5', 'f0ffff' );";  
			stmt.executeUpdate( query ); 
		
			query = "insert into Color (id, color) values('6',   'f5f5dc');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('7',   'ffe4c4');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('8',   '000000');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('9',   'ffebcd');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('10',  '0000ff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('11',  'a52a2a');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('12',  'deb887');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('13',  '5f9ea0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('14',  '7fff00');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('15',  'd2691e');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('16',  'ff7f50');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('17',  '6495ed');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('18',  'fff8dc');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('19',  'fff8dc');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('20',  'a52a2a');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('21',  '00ffff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('22',  '000088');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('23',  '008b8b');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('24',  'b8860b');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('25',  'a9a9a9');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('26',  '006400');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('27',  'bdb76b');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('28',  '8b008b');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('29',  '556b2f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('30',  'ff8c00');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('31',  '9932cc');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('32',  '8b0000');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('33',  'e9967a');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('34',  '8fbc8f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('35',  '483d8b');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('36',  '2f4f4f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('37',  '00ced1');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('38',  '9400d3');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('39',  'ff1493');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('40',  '00bfff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('41',  '696969');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('42',  '1e90ff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('43',  '822222');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('44',  'fffaf0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('45',  '228b22');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('46',  'ff00ff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('47',  'dcdcdc');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('48',  'f8f8ff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('49',  'ffd700');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('50',  'daa520');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('51',  '808080');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('52',  '008000');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('53',  'adff2f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('54',  'f0fff0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('55',  'ff69b4');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('56',  'cd5c5c');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('57',  '4b0082');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('58',  'fffff0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('59',  'f0e68c');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('60',  'e6e6fa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('61',  'fff0f5');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('62',  '7cfc00');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('63',  'fffacd');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('64',  'add8e6');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('65',  'f08080');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('66',  'e0ffff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('67',  'fafad2');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('68',  '90ee90');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('69',  'd3d3d3');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('70',  'ffb6c1');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('71',  'ffa07a');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('72',  '20b2aa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('73',  '87cefa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('74',  '778899');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('75',  'b0c4de');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('76',  'ffffe0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('77',  '00ff00');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('78',  '32cd32');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('79',  'faf0e6');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('80',  'ff00ff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('81',  '800000');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('82',  '66cdaa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('83',  '0000cd');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('84',  'ba55d3');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('85',  '9370d8');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('86',  '3cb371');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('87',  '7b68ee');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('88',  '00fa9a');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('89',  '48d1cc');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('90',  'c71585');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('91',  '191970');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('92',  'f5fffa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('93',  'ffe4e1');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('94',  'ffe4b5');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('95',  'ffdead');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('96',  '000080');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('97',  'fdf5e6');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('98',  '808000');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('99',  '6b8e23');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('100', 'ffa500');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('101', 'ff4500');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('102', 'da70d6');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('103', 'eee8aa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('104', '98fb98');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('105', 'afeeee');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('106', 'db7093');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('107', 'ffefd5');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('108', 'ffdab9');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('109', 'cd853f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('110', 'ffc0cb');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('111', 'dda0dd');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('112', 'b0e0e6');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('113', '800080');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('114', 'ff0000');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('115', 'bc8f8f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('116', '4169e1');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('117', '8b4513');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('118', 'fa8072');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('119', 'f4a460');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('120', '2e8b57');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('121', 'fffsee');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('122', 'a0522d');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('123', 'c0c0c0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('124', '87ceeb');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('125', '6a5acd');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('126', '708090');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('127', 'fffafa');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('128', '00ff7f');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('129', '4682b4');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('130', 'd2b48c');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('131', '008080');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('132', 'd8bfd8');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('133', 'ff6347');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('134', '40e0d0');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('135', 'ee82ee');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('136', 'f5deb3');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('137', 'ffffff');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('138', 'f5f5f5');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('139', 'ffff00');";	
			stmt.executeUpdate( query ); 
			query = "insert into Color (id, color) values('140', '9acd32');";	
			stmt.executeUpdate( query ); 
			
			con.commit();
			con.close();
		} catch( SQLException e ) {
			throw new CommandException( "fail to insert color" + query + e.toString() );
		}
	}


	/**
	 *  getColor  - color number를 얻는다.
	 *
	 *  @param  id  color id
	 *  @return color number
	 */
	public String getColor( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}
		try {
			String query = "select color from Color where id='" + id + "';";
			ResultSet result = stmt.executeQuery( query );

			String data = null;
			
			if( result.next() ) data = result.getString( "color" );
			else data = "no";
	
			con.close();
			return data;
		} catch( SQLException e ) {
			throw new CommandException( "fail to get Color number " + e.toString() );
		} catch( Exception e ) {
			throw new CommandException( "fail to get Color number " +   e.toString() );
		}	

	}

	/**
	 *  getCardColor -  명함색깔에 대한 정보를 제공한다.
	 * 
	 *  @param  id  사용자 아이디
	 */
	public ResultSet getCardColor( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}	

		try {
			String query = "select id, cardbg, cardfont, cardlogo from MemberPerson where id='" + id + "';";
			ResultSet result = stmt.executeQuery( query );
			con.close();
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to get Card Color " + e.toString() );
		} catch( Exception e) {
			throw new CommandException( "fail to get Card Color " + e.toString() );
		}
	}


	/**
	 *  setMyCardColor  - color 를 저장한다.
	 *
	 *  @param id      사용자 아이디
	 *  @param bgcolor 배경색
	 *  @param font    글자색 
	 */
	public void setMyCardColor( String id, String bgcolor, String font, String logo ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		try {
			if( bgcolor.equals( "" )) bgcolor = "ffffff";
			if( font.equals( "" ) ) font = "000000";
			if( logo.equals( "" ) ) logo= "none" ;

			String query = "update MemberPerson set mybg='" + bgcolor + "' " +
							" where id='" + id + "';";
			stmt.executeUpdate( query );
		
			query = "update MemberPerson set myfont='" + font + "' " +
					" where id='" + id + "';";
			stmt.executeUpdate( query );

			query = "select id, mylogo from MemberPerson where id='" + id + "';";
			ResultSet result = stmt.executeQuery( query );
			result.next();
			String orient = result.getString( "mylogo" );
			if( !orient.equals( "none") ) {	
				// 원래 있던 로고를지운다.
				try {
					File file = new File( PATH + uni.convert( orient ) );
					file.delete();
				} catch( Exception e ) {
					throw new CommandException( "fail to delete orient logo "  + e.toString() );
				}
			}
			query = "update MemberPerson set mylogo='" + logo + "' " +
				" where id='" + id + "';";
			stmt.executeUpdate( query );

			con.commit();
			con.close();
		} catch( SQLException e ) {
			throw new CommandException( "fail to set Color number " + e.toString() );
		} catch( Exception e ) {
			throw new CommandException( "fail to set Color number " + e.toString() );
		}	

	}


	/**
	 *  setCardColor  - color 를 저장한다.
	 *
	 *  @param id      사용자 아이디
	 *  @param bgcolor 배경색
	 *  @param font    글자색 
	 */
	public void setCardColor( String id, String bgcolor, String font, String logo ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		try {
			if( bgcolor.equals( "" )) bgcolor = "ffffff";
			if( font.equals( "" ) ) font = "000000";
			if( logo.equals( "" ) ) logo= "none";

			String query = "update MemberPerson set cardbg='" + bgcolor + "' " +
							" where id='" + id + "';";
			stmt.executeUpdate( query );
		
			query = "update MemberPerson set cardfont='" + font + "' " +
					" where id='" + id + "';";
			stmt.executeUpdate( query );

			query = "select id, cardlogo from MemberPerson where id='" + id + "';";
			ResultSet result = stmt.executeQuery( query );
			result.next();
			String orient = result.getString( "cardlogo" );
			if( !orient.equals("none") ) {	
				// 원래 있던 로고를지운다.
				try {
					File file = new File( PATH + uni.convert( orient ) );
					file.delete();
				} catch( Exception e ) {
					throw new CommandException( "fail to delete orient logo " + e.toString() );
				}
			}
			query = "update MemberPerson set cardlogo='" + logo + "' " +
				" where id='" + id + "';";
			stmt.executeUpdate( query );
			con.commit();
			con.close();
		} catch( SQLException e ) {
			throw new CommandException( "fail to set Color number " + e.toString() );
		} catch( Exception e ) {
			throw new CommandException( "fail to set Color number " + e.toString() );
		}	

	}

	//////////////////////////////  ComInfo  ///////////////////////////////
	
	/**
	 *  getCompanyInfo  - 회사정보를 넘겨주는 method
	 * 
	 *  @param  num  회사정보 고유 아이디 
	 */
	public ResultSet getCompanyInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select Company.id, Company.name, Company.region, Company.site, ComInfo.email, ComInfo.fax, ComInfo.tel, ComInfo.homepage, ComInfo.address  from ComInfo, Company, MemberPerson where MemberPerson.id='" + id + "' and Company.id=MemberPerson.com_id and ComInfo.id=Company.cominfo;" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! " + e.toString() );	
		} 

	}



	/**
	 *  getComInfo  - 회사정보를 넘겨주는 method
	 * 
	 *  @param  num  회사 고유 아이디 
	 */
	public ResultSet getComInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select *  from  ComInfo where Company.id='" + id + "' and ComInfo.id=Company.cominfo;" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to getComInfo in dbmanager! " + e.toString() );	
		} 

	}



	/**
	 *  getComChangeList  - 회사정보수정 신청 리스트를  넘겨주는 method
	 * 
	 */
	public ResultSet getComChangeList(  ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			 throw new CommandException( " fail to connect DB " + toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = "select ComInfo.id, Company.name, Company.site, Company.region, ComInfo.back " +
					"from ComInfo, Company " +
					" where Company.id=ComInfo.id;";
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to get ComChange in dbmanager! " + e.toString() );	
		} 
	}


	/**
	 *  getComChange - 회사정보수정 신청 리스트를  넘겨주는 method
	 * 
	 */
	public ResultSet getComChange( String id  ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			 throw new CommandException( " fail to connect DB " + toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = "select Company.name, Company.site, ComInfo.address, ComInfo.tel, ComInfo.fax, ComInfo.email, ComInfo.homepage, ComInfo.back from Company, ComInfo where Company.id='" + id + "'  and ComInfo.id='" + id + "' ;"; 
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to get ComChange in dbmanager! " + e.toString() );	
		} 
	}


	

	/**
	 *  addComInfo  - 새로운 회사정보를 등록하는  method
	 * 
	 *  @param  id        회사정보 고유 id
	 *  @param  address   주소
	 *  @param  tel       전화번호
	 *  @param  fax       팩스번호
	 *  @param  homepage  홈페이지 주소
	 *  @param  email     메일주소
	 *  @param  back      link 
	 */
	public void addComInfo( String id, String address, String tel, String fax, String homepage, String email, String back ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
					 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			int update;

			query = new String( "insert into ComInfo (id,address,tel,fax,homepage,email,back) values('"+id+"','"+address+"','"+tel+"','"+fax+"','"+homepage+"','"+email+"','"+back+"');");	
			update = stmt.executeUpdate( query );	
			
			if( update == 0 ) throw new CommandException( "fail to add new ComInfo " );

			// connection닫기
			stmt.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to sql statement at addComInfo: "  + e.toString() );	
		} catch( Exception e ) {
			throw new CommandException( "fail to addComInfo : " + e.toString() );
		} 

	}


	/**
	 *  deleteComInfo - company가 가지고 있던 회사정보를 모두 삭제하는 method
	 * 
 	 *  @param  id 최근 회사정보를 아이디
	 */
	public void deleteComInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;	
		String query = null;
		String select = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			String com = id;
			select = "select back from ComInfo where id=? ;";
			query  = "delete ComInfo where id= ? ;";

			stmt1 = con.prepareStatement( select );
			stmt2 = con.prepareStatement( query );
	
			while( !com.equals( "0" ) ) {			
				stmt1.setString( 1, com );
				stmt2.setString( 2, com );
				// com을 아이디로 하는 cominfo의 back을 얻는다. 
				result = stmt1.executeQuery();

				// jobinfo를 삭제한다.	
				if( stmt2.executeUpdate( ) == 0 ) 
					throw new CommandException( "fail to delete ComInfo");	
				com = result.getString( "back" ); 
			}	
			// connection닫기
			stmt1.close();
			stmt2.close();
			con.commit();	
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to delete JobInfo! " + e.toString() );	
		} 	
	}

	/**
	 *  deleteComChange - 회사정보수정 요청을 삭제하는  method
	 * 
 	 *  @param  id 최근 회사정보를 아이디
	 */
	public void deleteComChange( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();

			if( stmt.executeUpdate( "delete from ComInfo where id='" + id + "' and  back='comChange';" ) == 0 )
				throw new CommandException( "fail to delete ComChange ");
	
			// connection닫기
			stmt.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to delete comChange! " + e.toString() );	
		} 	
	}



	/**
	 *  updateComInfo - 새로운 회사정보를 저장하는  method
	 *
	 *  @param  comid     회사아이디
	 *  @param  address   주소  
	 *  @param  tel       전화번호
	 *  @param  fax       팩스번호
	 *  @param  homepage  홈페이지 
	 *  @param  eamil     이메일  
	 */
	public void updateComInfo( String comid, String address, String tel, String fax, String homepage, String email, String back) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		ResultSet com = null;
		ResultSet comInfo = null;
		String current = null;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			// 현재 등록된 comInfo를 얻는다. 
			com = stmt.executeQuery( "select cominfo from Company where id='" + comid + "';" );
			if( !com.next() ) throw new CommandException( "fail to get cominfo in Company "  );

			// 새로 등록될 jobInfo에 back으로 등록될 jobInfo reference	
			current = com.getString( "cominfo" );

			String query = "";
	
			if( back.length() > 9 ){
				String c_site = back.substring( 10 );	
				query = "update Company set site='" + c_site + "'" +
								" where id='" + comid + "';";
				stmt.executeUpdate( query );
			}	
			
			// object생성 시간 입력
			Calendar date = Calendar.getInstance();
		
			String time = String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
			String subcom = comid.substring( 0, 5 ); 	
			String new_comInfo =  time + subcom;	
			//  새로운 jobInfo 등록	
			query = "insert into ComInfo (id, address, tel, fax, homepage, email, back)" +
					" values('"+new_comInfo+"', '"+address + "', '" + tel + "','" + fax +
					"', '" + homepage + "', '" + email + "', '" + current + "' );";
			stmt.executeUpdate( query );
					
			// MemberPerson에 최신 jobInfo update	
			query = "update Company set cominfo='" + new_comInfo + 
					"' where id='" + comid + "';" ; 
			stmt.executeUpdate( query );
		
			// request list에 있던 object를 삭제한다.	
			query = "delete from ComInfo where id='" + comid + "';";
			stmt.executeUpdate( query );

			// connection닫기
			stmt.close();
			con.commit();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to update ComInfo! "  + e.toString());	
		} 	

}


	/////////////////////////////  Company  ///////////////////////////

	/**
	 *  getCompany  - 회사정보를 넘겨주는 method
	 * 
	 *  @param  num 회사 아이디
	 */
	public ResultSet getCompany( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con;
		Statement stmt;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
		con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}


		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select * from Company where id='" + id + "';" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! "  + e.toString());	
		} 
	}

	/**
	 *  searchCompany  - 회사가 존재하는지 확인하는 method
	 *
 	 *  @param  name    회사이름
	 *  @param  region  지역
	 *  @param  site    지점
	 */ 
	public ResultSet searchCompany( String name, int region, String c_site )
						throws CommandException   {
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
	    Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;
		ResultSet r = null;
	
		try {	
			// DB connection을 연다	
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB at DBManager " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = "select * from Company where name='" +name +
					"' and region=" + region + 
					" and site='" + c_site+ "';";

			result = stmt.executeQuery( query );	

			// connection닫기
			stmt.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! " + e.toString() );	
		} 
		return result;	
	}
	


	/**
	 *	addCompany - 회사를 Company DB에 추가한다.
	 *	
	 * 	@param id 		회사 아이디
	 * 	@param name 	회사 이름
	 * 	@param site 	회사 지점
	 * 	@param region 	지역
	 * 	@param comInfo	comInfo id
	 *
	 */
	public void addCompany( String com_id, String name, String site, int region, String comInfo )throws CommandException{

		Connection con = null;
		Statement stmt = null;


		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}

		try{	 
			//query statement
			String query = "insert into Company (id, name, site, region, comInfo, employee) values ('" + com_id + "', '" + name + "', '" + site + "', " + region + ", '" + comInfo + "', '0');";

			//qeury 문 실행
			stmt.executeUpdate( query );
		
		}catch( SQLException e ){
			throw new CommandException( "fail to insert into Company " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addCompany " + e.toString() );
		}
	}



	/**
	 *  deleteCompany  - 회사를 삭제하는  method
	 * 
	 *  @param  id  회사아이디
	 */
	public void deleteCompany( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		try {
			// company를 얻는다.
			ResultSet company = stmt.executeQuery( "select employee from Company where id='" + id + "';" );
			if( !company.next() ) 
				throw new CommandException( "fail to get employee number from company " );
			// 회사정보를 먼저 삭제한다.
			deleteComInfo( company.getString( "comInfo" ) );

			// 회사를 삭제한다.
			if( stmt.executeUpdate( "delete Company where id='" + id + "';" ) == 0 ) 
				throw new CommandException( "fail to delete Company"  ); 
			stmt.close();
			con.commit();
			con.close();	
		} catch( SQLException e ) {
			throw new CommandException( "fail to query" + e.toString() );
		}
	}


	/**
	 *  increaseEmployee  - 직원수를 늘리는  method
	 * 
	 *  @param  id  회사아이디
	 */
	public void increaseEmployee( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con;
		Statement stmt;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		try {
			// company를 얻는다.
			ResultSet company = stmt.executeQuery( "select employee from Company where id='" + id + "';" );
			if( !company.next() ) 
				throw new CommandException( "fail to get employee number from company " );
	
			// company에서 직원수를 얻어와서 1 증가시킨다.
			int employee = Integer.parseInt( company.getString( "employee" ) );
			employee++;

			if( stmt.executeUpdate( "update Company set employee='" + employee + "' where id='" + id + "';" ) == 0 )
				throw new CommandException( "fail to increase employee number" );
			// connetion을 commit하고 닫는다.	
			stmt.close();
			con.commit();
			con.close();			
		} catch( SQLException e ) {
			throw new CommandException( "fail to query" + e.toString() );
		}
		
	}


	/**
	 *  decreaseEmployee  - 직원수를 줄이는  method
	 * 
	 *  @param  id  회사아이디
	 */
	public void decreaseEmployee( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		try {
			// company를 얻는다.
			ResultSet company = stmt.executeQuery( "select employee from Company where id='" + id + "';" );
			if( !company.next() ) 
				throw new CommandException( "fail to get employee number from company " );
				
			// company에서 직원수를 얻어와서 1 증가시킨다.
			int employee =  company.getInt( "employee" ) ;
			employee = employee - 1;

			// 회사에 등록된 직원이 한명도 없는 경우
			stmt.executeUpdate( "update Company set employee='" + employee + "' where id='" + id + "';" );

			// connection 을 commit하고 닫는다.	
			stmt.close();
			con.commit();
			con.close();
		} catch( SQLException e ) {
			throw new CommandException( "fail to query" + e.toString() );
		}
		


	}


	/////////////////////////////  Directory  ///////////////////////////////

	/**
	 *	getDirList - 명함 디렉토리 리스트를 얻어 리턴한다.
	 *
	 *	@param id 명함주인 아이디
	 *
	 */
	public ResultSet getDirList( String id ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result;
	
		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}


		try{
			//DB query statement
			String query = " select * from Directory where owner ='" + id + "' order by name ;";

			//DB query
			result = stmt.executeQuery( query );
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to select dir name" + e.toString() ) ;
		}catch( Exception e ){
			throw new CommandException( "fail to getDirList " + e.toString() );
		}	 

		return result;
	}

	/**
	 *	getDir - 명함 디렉토리를 얻어 리턴한다.
	 *
	 *	@param dir_id 디렉토리 아이디
	 *
	 */
	public ResultSet getDir( String dir_id ) throws CommandException{

		Connection con;
		Statement stmt;
		ResultSet result;
	
		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();

		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}


		try{
			//DB query statement
			String query = " select * from Directory where id ='" + dir_id + "';";

			//DB query
			result = stmt.executeQuery( query );
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to select dir " + e.toString() ) ;
		}catch( Exception e ){
			throw new CommandException( "fail to getDir " + e.toString() );
		}	 

		return result;
	}

	/**
	 *	addDir - 디렉토리 추가한다.
	 *
	 *	@param id 		디렉토리 주인 
	 *	@param dir_id 	디렉토리 아이디 
	 *	@param name 	디렉토리 이름
	 *
	 */
	public void addDir( String id, String dir_id, String name )throws CommandException{

		Connection con;
		Statement stmt;

		
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}

		//새로운 디렉토리를 DB에 추가한다.
		try{
			String select = "select * from Directory where owner='" + id + "' and name='" + name + "';";
			ResultSet result = stmt.executeQuery( select );
			if( result.next() ) {
				throw new CommandException( uni.convert("이미 ") +name+ uni.convert("디렉토리가 존재합니다" ));
			}
			//query statement
			String query = "insert into Directory (id, name, number, owner) values ('" + dir_id + "', '" +  uni.convert( name )  + "', 0, '" + id + "');";

			//query 문 실행
			stmt.executeUpdate( query );			
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addDir at DBManager " + e.toString() );
		}
	}

	/**
	 *	deleteDir - 디렉토리 삭제한다.
	 *
	 *	@param id		명함주인 아이디
	 *	@param dir_id	디렉토리 id
	 *
	 */
	public void deleteDir( String dir_id )throws CommandException{

		Connection con;
		Statement stmt;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		//디렉토리를 db에서 삭제한다.
		try{
			con.setAutoCommit( false );
	
			String message = " in query1";
			String query1 = "select NonMemberPerson.id, NonMemberPerson.personinfo, " +
					" NonMemberPerson.jobinfo, NonMemberPerson.com_id, " +
					" Card.person_kind " +
					" from Card, NonMemberPerson where Card.dir_id='" + dir_id + 
					"' and NonMemberPerson.id=Card.card_id;";
			ResultSet card = stmt.executeQuery( query1 );

			// card지울 때 관련 정보도 삭제한다. 
			while( card.next()) {
				// nonmember인 경우만 그러하므로 person_kind를 확인한다. 
				if( card.getString( "person_kind" ).equals( "n"  )){
					// personinfo삭제
					message = " in query2";
					String query2 = "delete from PersonInfo where id='" + card.getString( "personinfo" ) + "';";	
					stmt.executeUpdate( query2 );


					// jobinfo삭제
					message = " in query3 ";
					String query3 = "delete from JobInfo where id='" + card.getString( "jobinfo" ) + "';";
					stmt.executeUpdate( query3 );

					// company를 조사한다.
					message = " in query4";
					String query4 = "select employee, cominfo from Company where id='" + card.getString( "com_id" ) + "';";
					ResultSet com = stmt.executeQuery( query4 );

					int employee = 0;
					if( com.next() ) {
						employee = com.getInt( "employee" );
					}	
					// 직원이 1명뿐이면 회사도 삭제한다.
					if( employee == 1 ) {
						ResultSet cominfo = null;
						String query5 = null;
						String query6 = null;
 
						// connection에서 statement를 생성해서 query한다.
						String back = com.getString( "cominfo" );

						// 'none'이면 cominfo가 없으므로 'none'이 아닐 경우만 처리
						if( !back.equals( "none" ) ){
							while( !back.equals( "0" ) ) {			
								// com을 아이디로 하는 cominfo의 back을 얻는다. 
								message = " in query5";
								query5 = "select back from ComInfo where id='" + back + "';";

								cominfo = stmt.executeQuery(query5 );
								cominfo.next();

								// cominfo를 삭제한다.	
								message = "in query6";
								query6 = "delete from ComInfo where id='" + back + "';";
								stmt.executeUpdate( query6 ); 
								back = cominfo.getString( "back" ); 
		
							}	
						}	
						// Company를 삭제한다.
						message = "in query7";
						String query7 = "delete from Company where id='" + card.getString( "com_id" ) + "';";
						stmt.executeUpdate( query7 );
	
					} else { 
						// company에서 직원수를 얻어와서 1 감소시킨다.
						employee = employee - 1;

						message = " in query8";
						String query8 = "update Company set employee='" + employee + "' where id='" + card.getString( "com_id" )  + "';"; 
						// 회사에 등록된 직원이 한명도 없는 경우
						stmt.executeUpdate( query8); 	
					} // end of if-else
					String query9 = "delete from NonMemberPerson where id='" + card.getString( "id" ) + "';";
					stmt.executeUpdate( query9 );
				}	

			} // cardlist
	
			//query statement
			String query10 = "delete from Card where dir_id='" + dir_id + "';";
			String query11 = "delete from Directory where id='" + dir_id + "';"; 

			//query 문을 실행한다. 
			stmt.executeUpdate( query10 );
			stmt.executeUpdate( query11 );
			
			con.commit();
			con.setAutoCommit( true );
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to execute delete " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to deleteDir at DBManager " + e.toString() );
		}
	}

	/**
	 *	updateDir - 디렉토리를 수정한다.
	 *
	 *	@param dir_id 		수정할 디렉토리 아이디
	 *	@param name 	수정할 디렉토리 이름
	 *	@param num 		수정할 디렉토리내의 카드수
	 *
	 */
	public void updateDir( String dir_id, String name, int num ) throws CommandException{

		Connection con;
		Statement stmt;

		//DB connection
		try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		    	throw new CommandException( e.toString() );
		}

		try{
			//query statement
			String query = "update Directory set name = '" + name + "', number = '" + num + "' where id = '" + dir_id + "';";

			//query 문 실행
			stmt.executeUpdate( query );
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to update execute " + e.toString() );
		}catch( Exception  e ){
			throw new CommandException( "fail to updateDir at DBManager " + e.toString() );
		}
	}

	



	////////////////////////////  IDReqList  //////////////////////////

	/**
	 *  getIDInfo  - 아이디 신청 리스트를  넘겨주는 method
	 *
	 *  @param  id  아이디 신청자의 아이이
	 *  @throws CommandException DB접속에 문제가 생길 때 
	 */
	public ResultSet getIDInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con;
		Statement stmt;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select *  from IDReqList where id='" + id + "';" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! " + e.toString() );	
		} 
	}

	/**
	 *  getIDRequest  - 아이디 신청 리스트를  넘겨주는 method
	 *
	 *  @param  id  아이디 신청자의 아이이
	 *  @throws CommandException DB접속에 문제가 생길 때 
	 */
	public ResultSet getIDRequest( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con;
		Statement stmt;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = "select IDReqList.id, IDReqList.name, Company.name, JobInfo.part, "+
					" JobInfo.posi, PersonInfo.address, PersonInfo.email, PersonInfo.tel " +
					" from IDReqList, Company, JobInfo, PersonInfo " +
					" where IDReqList.id='" + id + "' " +
					" and PersonInfo.id=IDReqList.personinfo " +
					" and JobInfo.id=IDReqList.jobinfo " +
					" and Company.id=IDReqList.com_id;";	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! " + e.toString() );	
		} 
	}


	/**
	 *	addIDRequest - ID 신청을 DB에 등록하는 method 
	 *
	 *	@param id 	       아이디	
	 *	@param password    패스워드 
	 *	@param name        이름 
	 *	@param ssn         주민등록번호
	 *	@param sex         성별
	 *	@param birth        생년월일 
	 *	@param description 자기소개말 
	 *	@param personInfo  개인정보 
	 *  @param jobInfo     직장정보
	 */
	public void addIDRequest( String id, String password, String name, String ssn, String sex, String birth, String description, String personInfo, String jobInfo )throws CommandException{
		Connection con = null;
		Statement stmt = null;

		
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}

		//새로운 jobInfo를 DB에 추가한다.
		try{
			//query statement
			String query = "insert into IDReqList (id, password, name, ssn, sex, birthday, description, personinfo, jobInfo) values ('" + id + "', '" + password + "', '" + name + ", '" + ssn + "', '" + sex + "', '" + birth + "', '" + description + "', '" + personInfo + "', '" + jobInfo + "');";

			//query 문 실행
			if( stmt.executeUpdate( query ) == 0 ) 
				throw new CommandException( "fail to regist IDRequest " ); 			
			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addIDReq " + e.toString() );
		}
	}

	/**
	 *	deleteIDRequest - ID 신청을 DB에서 삭제하는  method 
	 *
	 *	@param id 삭제할 아이디	
	 */
	public void deleteIDRequest( String id )throws CommandException{
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
		Connection con = null;
		Statement stmt = null;
		
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );	
		} catch( SQLException e ) {
			throw new CommandException( e.toString() );
		}

		//새로운 jobInfo를 DB에 추가한다.
		try{
			//query statement
			String query = "delete from IDReqList  where id='" + id + "';";

			//query 문 실행
			if( stmt.executeUpdate( query ) == 0 ) 
				throw new CommandException( "fail to regist IDRequest " );

			// commit하고 AutoCommit를 false로 한다.	
			con.commit();
			con.setAutoCommit( true );
			
			stmt.close();
			con.commit();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addIDReq " + e.toString() );
		}
	}

	/**
	 *	cancelIDRequest - ID 신청을 DB에서 삭제하는  method 
	 *
	 *	@param id 삭제할 아이디	
	 */
	public void cancelIDRequest( String id )throws CommandException{
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		String message = null;
	
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );	
		} catch( SQLException e ) {
			throw new CommandException( e.toString() );
		}

		//새로운 jobInfo를 DB에 추가한다.
		try{
			// 먼저 관련정보를 지우기 위해 정보를 얻는다.
			message = " in query1 ";

			String query1 = "select * from IDReqList where id='" + id + "';";
			result = stmt.executeQuery( query1 ); 

			if( result.next() ) message = " result is not null";
		
			message += " in query2 ";
			// personinfo를 삭제한다.
			String query2 = "delete from PersonInfo where id='" + result.getString( "personinfo" ) + "';";
			stmt.executeUpdate( query2 );
	
			message += " in query3 ";
			// jobinfo를 삭제한다.
			String query3 = "delete from JobInfo where id='" + result.getString( "jobinfo" ) + "';";
			stmt.executeUpdate( query3 );
		
			message = " in query4 ";
			// company를 조사한다.
			String query4 = "select employee, cominfo from Company where id='" + result.getString( "com_id" ) + "';";
			ResultSet com = stmt.executeQuery( query4 );
			com.next();
			int employee = com.getInt( "employee" );

			// 직원이 1명뿐이면 회사도 삭제한다.
			if( employee == 1 ) {
				ResultSet cominfo = null;
				String query5 = null;
				String query6 = null;
 
				// connection에서 statement를 생성해서 query한다.
				try {
					String back = com.getString( "cominfo" );
					if( !back.equals( "none" ) ) {	
						while( !back.equals( "0" ) ) {			
							message = " in query5 ";
							query5 = "select back from ComInfo where id='" + back + "';";
							query6 = "delete from ComInfo where id='" + back + "';";
		
							// com을 아이디로 하는 cominfo의 back을 얻는다. 
							cominfo = stmt.executeQuery( query5 );
							cominfo.next();

							message = " in query6 ";
							// jobinfo를 삭제한다.	
							stmt.executeUpdate( query6 ); 
							back = cominfo.getString( "back" ); 
						}	
					}
				} catch( SQLException e ) {
					throw new CommandException( "fail to delete ComInfo in cancelIDRequest! " + e.toString() );	
				} 	

				message = " in query7 ";
				// Company를 삭제한다.
				String query7 = "delete from Company where id='" + result.getString( "com_id" ) + "';";
				stmt.executeUpdate( query7 );
			} else { 
				// company에서 직원수를 얻어와서 1 감소시킨다.
				message = " in query8 ";
				employee -= 1;
				String query8 = "update Company set employee='" + employee + "' where id='" + result.getString( "com_id" )  + "';"; 
				stmt.executeUpdate( query8 ); 	
			} // end of if-else

			// IDRequest 를 삭제한다.
			message = " in query9";
			String query9 = "delete from IDReqList where id='" + id + "';";
			stmt.executeUpdate( query9 );


			// commit하고 AutoCommit를 false로 한다.	
			stmt.close();
			con.commit();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query in DBManager" + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to cancel IDReq in DBManager" + message + e.toString() );
		}
	}

		


	/**
	 *  getIDReqList  - 아이디 신청 리스트를  넘겨주는 method
	 * 
	 */
	public ResultSet getIDReqList(  ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con;
		Statement stmt;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select IDReqList.id, IDReqList.name, Company.name, Company.site, JobInfo.part, JobInfo.posi from IDReqList, Company, JobInfo where Company.id=IDReqList.com_id and JobInfo.id=IDReqList.jobinfo ;" );
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to get IdReqList! " + e.toString() );	
		} 
	}


	
	//////////////////////////  JobInfo  //////////////////////////

	/**
	 *  getJobInfo  - 회사정보를 넘겨주는 method
	 * 
	 *  @param  id   user 고유 아이디 
	 */
	public ResultSet getJobInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = "select * from JobInfo where MemberPerson.id='" + id + "' and JobInfo.id=MemberPerson.jobinfo;" ;	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to getJobInfo at sql! "  + e.toString());	
		} catch( Exception e ){
			throw new CommandException( "fail to getJobInfo!" + e.toString() );
		}

	}

	/**
	 *  getJobChange  - 회사정보를 넘겨주는 method
	 * 
	 *  @param  id   jobinfo 고유 아이디 
	 */
	public ResultSet getJobChange( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query = null;	
		ResultSet result = null;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = "select MemberPerson.id, MemberPerson.name, MemberPerson.com_id, " +
					"Company.name, Company.region, ComInfo.tel, " +
					"ComInfo.fax, ComInfo.email, ComInfo.homepage, ComInfo.address, " +
					"JobInfo.tel, JobInfo.fax,  " +
					" Company.employee, Company.site, JobInfo.kind " +
					" from  MemberPerson, Company, ComInfo, JobInfo " +
					" where MemberPerson.id='" + id + "'" +
					" and JobInfo.id=MemberPerson.jobinfo " +
					" and PersonInfo.id=MemberPerson.personinfo " +
					" and Company.id=MemberPerson.com_id" +
					" and ComInfo.id=Company.cominfo;"; 

			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to getjobChange at sql! "  + e.toString());	
		} catch( Exception e ){
			throw new CommandException( "fail to getJobInfo!" + e.toString() );
		}

	}

	/**
	 *	addJobInfo - job info를 JobInfo DB에 추가한다.
	 *
	 *	@param id 			jobInfo id
	 *	@param position 	지위
	 *	@param part 		부서
	 *	@param tel 			전화번호
	 *	@param fax 			팩스
	 *	@param kind 		업종
	 *	@param com_id 		회사 아이디
	 *  @param back         이전 회사정보 
	 */
	public void addJobInfo( String id, String position, String part, String tel, String fax, int kind, String com_id, String back )throws CommandException{

		Connection con = null;
		Statement stmt = null;

		
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}

		//새로운 jobInfo를 DB에 추가한다.
		try{
			//query statement
			String query = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + id + "', '" + position + "', '" + part + "', '" + tel + "', '" + fax + "', '" + kind + "', '" + com_id + "', '" + back + "');";

			//query 문 실행
			stmt.executeUpdate( query );			

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addJobInfo at DBManager " + e.toString() );
		}
	}

	/**
	 *  deleteJobInfo - user가 가지고 있던  직장정보를 모두 삭제하는 method
	 * 
 	 *  @param  id 최근 직장정보를 아이디
	 *  @throws CommandException  DB와의 접속에 문제가 있는 경우
	 */
	public void deleteJobInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		PreparedStatement stmt1, stmt2;
		ResultSet result = null;		 
		String select, query;
		 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			con.setAutoCommit( false );	
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			select = "select back from JobInfo where id = ?;" ; 	
			query  = "delete JobInfo where id = ? ;";	
			String job = id;
			stmt1 = con.prepareStatement( select );
			stmt2 = con.prepareStatement( query );	

			// 링크를 따라가면서 다 지운다.	
			while( !job.equals( "0" ) ) {			
				stmt1.setString( 1, job );
				result = stmt1.executeQuery( ); 	
				
				// jobinfo를 삭제한다.	
				stmt2.setString( 1, job );	
				if( stmt2.executeUpdate() == 0 )
					throw new CommandException( "fail to delete JobInfo");	
				job = result.getString( "back" ); 
			}
		
			// commit하고 autocommit를 다시 true로 한다.
			con.commit();
			// connection닫기
			stmt1.close();
			stmt2.close();	
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to delete JobInfo! "  + e.toString());	
		} 	
	}


	/**
	 *  updateJobInfo - 새로운 회사정보를 저장하는  method
	 *
	 *  @param  id  개인정보를 바꿀 사용자의 아이디
	 *  @param  tel   새로운 주소
	 *  @param  fax 
	 *  @param  position 
	 *  @param  part 
	 */
	public void updateJobInfo( String id, String tel, String fax, String position, String part) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		ResultSet member = null;
		String current = null;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			// 현재 등록된 jobInfo를 얻는다. 
			member = stmt.executeQuery( "select MemberPerson.jobinfo, JobInfo.kind, JobInfo.company  from MemberPerson, JobInfo where MemberPerson.id='" + id + "' and JobInfo.id=MemberPerson.jobinfo;" );
			if( !member.next() ) throw new CommandException( "fail to get MemberPerson " );

			// 새로 등록될 jobInfo에 back으로 등록될 jobInfo reference	
			current = member.getString( "jobinfo" );
			
			// object생성 시간 입력
			Calendar date = Calendar.getInstance();
		
			String time = String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
		
			String new_jobInfo = time + id;	

			//  새로운 jobInfo 등록	
			String query = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + new_jobInfo + "', '" + position + "', '" + part + "', '" + tel + "', '" + fax + "', '" + member.getString( "kind" ) + "', '" + member.getString( "company" ) + "', '" + current + "');";

			//query 문 실행
			stmt.executeUpdate( query );			


			// MemberPerson에 최신 jobInfo update	
			if( stmt.executeUpdate( "update MemberPerson set jobinfo='" + new_jobInfo + "' where id='" + id + "';" )  == 0 )			
				throw new CommandException( "fail to update JobInfo in dbmanager"  );

			// commit
			con.commit();
			// connection닫기
			stmt.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to update JobInfo in dbmanager"  + e.toString());	
		} 	


	}
			
	/**
	 *  updateJobInfo - 새로운 회사정보를 저장하는  method
	 *
	 *  @param  id        개인정보를 바꿀 사용자의 아이디
	 *  @param  position  직책
	 *  @param  part      부서
	 *  @param  tel       전화번호 
	 *  @param  fax       팩스 
	 *  @param  kind      업종
	 *  @param  company   새로운 회사아이디 
	 */
	public void updateJobInfo( String id, String position, String part,  String tel, String fax, String kind, String company ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		ResultSet member = null;
		String current = null;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			// 현재 등록된 jobInfo를 얻는다. 
			member = stmt.executeQuery( "select jobinfo from MemberPerson where id='" + id + "';" );
			if( !member.next() ) throw new CommandException( "fail to get MemberPerson " );

			// 새로 등록될 jobInfo에 back으로 등록될 jobInfo reference	
			current = member.getString( "jobinfo" );
			
			// object생성 시간 입력
			Calendar date = Calendar.getInstance();
		
			String time = String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
		
			String new_jobInfo = time + id;	

			//  새로운 jobInfo 등록	
			String query = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + new_jobInfo + "', '" + position + "', '" + part + "', '" + tel + "', '" + fax + "', '" + Integer.parseInt( kind ) + "', '" + company + "', '" + current + "');";

			//query 문 실행
			stmt.executeUpdate( query );			


			// MemberPerson에 최신 jobInfo update	
			if( stmt.executeUpdate( "update MemberPerson set jobinfo='" + new_jobInfo + "' where id='" + id + "';" )  == 0 )			
				throw new CommandException( "fail to update JobInfo in dbmanager"  );
			stmt.executeUpdate( "update MemberPerson set com_id='" + company + "' where id='" + id + "';" );

			// commit
			con.commit();
			// connection닫기
			stmt.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to update JobInfo in dbmanager"  + e.toString());	
		} 	


	}
	
	

	//////////////////////  MemberPerson  //////////////////////////

	/**
	 *  getMemberPerson  - member 정보를 넘겨주는 method
	 * 
	 *  @param  id   member 아이디 
	 */
	public ResultSet getMemberPerson( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	System.err.print( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select * from MemberPerson where id='" + id + "';" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! "  + e.toString());	
		} 
	}

	/**
	 *  getMemberInfo  - member 정보를 넘겨주는 method
	 * 
	 *  @param  id   member 아이디 
	 */
	public ResultSet getMemberInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	System.err.print( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query =  "select MemberPerson.id, MemberPerson.password, MemberPerson.name,  " + 
					"MemberPerson.ssn, MemberPerson.birthday, MemberPerson.sex, " +
					"PersonInfo.address, PersonInfo.tel, PersonInfo.beeper, " +
					"PersonInfo.handphone, PersonInfo.homepage, PersonInfo.email, " +
					"JobInfo.posi, JobInfo.part, JobInfo.tel, JobInfo.fax,  " +
					" Company.name,  " +
					" MemberPerson.mybg, MemberPerson.myfont, MemberPerson.mylogo " +
					"from MemberPerson, PersonInfo, JobInfo, Company " +
					"where MemberPerson.id='" + id + 
					"' and PersonInfo.id=MemberPerson.personinfo " +
					" and Company.id=MemberPerson.com_id " +
					"and JobInfo.id=MemberPerson.jobinfo;";	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! "  + e.toString());	
		} 
	}


	/**
	 *	addMemberPerson - ID 신청 정식아이디로 DB에 등록하는 method 
	 *
	 *	@param id 등록할 아이디	
	 *
	 *  @throws CommandException DB와의 접속에 문제가 있을 경우 발생
	 */
	public void addMemberPerson( String id )throws CommandException{
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
		
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;		 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );	
		} catch( SQLException e ) {
		  	throw new CommandException( e.toString() );
		}

		//새로운 jobInfo를 DB에 추가한다.
		try{
			result = getIDInfo( id );
			result.next();	
			//query statement
			String query = "insert into MemberPerson (id, password, name, ssn, sex, birthday, description, personinfo, jobinfo, com_id) values ('" + result.getString( "id" ) + "', '" + result.getString( "password" ) + "', '" + result.getString( "name" ) + "', '" + result.getString( "ssn" ) + "', '" + result.getString( "sex" ) + "', '" + result.getString( "birthday" ) + "', '" + result.getString( "description" ) + "', '" + result.getString( "personinfo" ) + "', '" + result.getString( "jobinfo" ) + "', '" + result.getString( "com_id" ) + "' );";

			//query 문 실행
			if( stmt.executeUpdate( query ) == 0 ) 
				throw new CommandException( "fail to regist IDRequest "  );	

			// commit하고 connection을 닫는다.
			con.commit();
			stmt.close();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query  in addMemberPerson" + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addmemberPerson" + e.toString() );
		}
	}


	/**
	 *	deleteMemberPerson - ID 를 DB에서 삭제하는  method 
	 *
	 *	@param id 삭제할 아이디	
	 *  @throws CommandException DB와의 접속에 문제가 있을 경우 발생
	 */
	public void deleteMemberPerson( String id )throws CommandException{
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}

		Connection con = null;
		Statement stmt = null;
		ResultSet member = null;
		ResultSet result = null;
		ResultSet com = null;
		ResultSet card = null;


	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );	
		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}

		String message = null;
		// id와 관련된 정보를 삭제한다.
		try{
			member = getMemberPerson( id );
			member.next();

			// 가져고 있던 모든 정보를 삭제한다.
			String person = member.getString( "personinfo" );
			String select1 =  null;
			String query1  =  null;
			int employee = 0;

			message = " in query1 ";
			// personinfo삭제	
			while( !person.equals( "0" ) ) {			
				select1 = "select back from PersonInfo where id='" + person  + "';";
				result = stmt.executeQuery( select1 ); 	
				result.next();	
				query1 = "delete from PersonInfo where id='" + person + "';";	
				stmt.executeUpdate( query1 );
				person = result.getString( "back" ); 
			}
	
			// jobinfo를 삭제한다.
			message = " in query2 ";
			String select2 = null;	
			String query2  = null ;	
			String job = member.getString( "jobinfo" );

			// 링크를 따라가면서 다 지운다.	
			while( !job.equals( "0" ) ) {			
				message = " in select2 ";
				select2 = "select back from JobInfo where id='" + job + "';"; 
				result = stmt.executeQuery( select2 ); 	
				result.next();
				message = " in query2 after select2 ";
				query2 = "delete from JobInfo where id='" + job + "';";	
				stmt.executeUpdate( query2 );
				job = result.getString( "back" ); 
			}
			
			// company를 조사한다.
			message = " in query3";
			String query3 = "select employee, cominfo from Company where id='" + member.getString( "com_id" ) + "';";
			com = stmt.executeQuery( query3 );
			if( com.next() ) {
				employee = com.getInt( "employee" );
			}
			// 직원이 1명뿐이면 회사도 삭제한다.
			if( employee == 1 ) {
				ResultSet cominfo = null;
				String query4 = null;
				String query5 = null;
 
				// connection에서 statement를 생성해서 query한다.
				String back = com.getString( "cominfo" );

				while( !back.equals( "0" ) ) {			
					message = " in query4";
					query4 = "select back from ComInfo where id='" + back + "';";
					query5 = "delete from ComInfo where id='" + back + "';";
	
					// com을 아이디로 하는 cominfo의 back을 얻는다. 
					cominfo = stmt.executeQuery( query4 );
					cominfo.next();

					// cominfo를 삭제한다.	
					message = "in query5";
					stmt.executeUpdate( query5 ); 
					back = cominfo.getString( "back" ); 

				}	

				// Company를 삭제한다.
				message = "in query6";
				String query6 = "delete from Company where id='" + member.getString( "com_id" ) + "';";
				stmt.executeUpdate( query6 );

			} else { 
				// company에서 직원수를 얻어와서 1 감소시킨다.
				employee = employee - 1;

				message = " in query7";
				String query7 = "update Company set employee='" + employee + "' where id='" + member.getString( "com_id" )  + "';"; 
				// 회사에 등록된 직원이 한명도 없는 경우
				stmt.executeUpdate( query7 ); 	
			} // end of if-else

			// 등록된 디렉토리를 얻는다.
			message = " in query8 ";
			String query8 = "select id from Directory where owner='" + id + "';";
			ResultSet directory = stmt.executeQuery( query8 );
			String dir_id = null;

			// 디렉토리하나마다 등록된 명함을 삭제하고 디렉토리를 삭제한다. 
			while( directory.next() ) {
				dir_id = directory.getString( "id" );
		
				String clear0 = null;	
				String clear1 = null;
				String clear2 = null;
				message = " in clear0";
				clear0 = "select NonMemberPerson.id, NonMemberPerson.personinfo, " +
						" NonMemberPerson.jobinfo, NonMemberPerson.com_id, " +
						" Card.person_kind " +
						" from Card, NonMemberPerson where Card.dir_id='" + dir_id + 
						"' and NonMemberPerson.id=Card.card_id;";
				card = stmt.executeQuery( clear0 );

				// card지울 때 관련 정보도 삭제한다. 
				while( card.next()) {
					// nonmember인 경우만 그러하므로 person_kind를 확인한다. 
					if( card.getString( "person_kind" ).equals( "n"  )){
						// personinfo삭제
						message = " in clear1";
						clear1 = "delete from PersonInfo where id='" + card.getString( "personinfo" ) + "';";	
						stmt.executeUpdate( clear1 );

	
						// jobinfo삭제
						message = " in clear2 ";
						clear2 = "delete from JobInfo where id='" + card.getString( "jobinfo" ) + "';";
						stmt.executeUpdate( clear2 );
	
						// company를 조사한다.
						message = " in clear3";
						String clear3 = "select employee, cominfo from Company where id='" + card.getString( "com_id" ) + "';";
						com = stmt.executeQuery( clear3 );

						employee = 0;
						if( com.next() ) {
							employee = com.getInt( "employee" );
						}	
						// 직원이 1명뿐이면 회사도 삭제한다.
						if( employee == 1 ) {
							ResultSet cominfo = null;
							String clear4 = null;
							String clear5 = null;
	 
							// connection에서 statement를 생성해서 query한다.
							String back = com.getString( "cominfo" );
	
							// 'none'이면 cominfo가 없으므로 'none'이 아닐 경우만 처리
							if( !back.equals( "none" ) ){
								while( !back.equals( "0" ) ) {			
									// com을 아이디로 하는 cominfo의 back을 얻는다. 
									message = " in clear4";
									clear4 = "select back from ComInfo where id='" + back + "';";

									cominfo = stmt.executeQuery(clear4 );
									cominfo.next();
	
									// cominfo를 삭제한다.	
									message = "in clear5";
									clear5 = "delete from ComInfo where id='" + back + "';";
									stmt.executeUpdate( clear5 ); 
									back = cominfo.getString( "back" ); 
			
								}	
							}	
							// Company를 삭제한다.
							message = "in clear6";
							String clear6 = "delete from Company where id='" + card.getString( "com_id" ) + "';";
							stmt.executeUpdate( clear6 );
	
						} else { 
							// company에서 직원수를 얻어와서 1 감소시킨다.
							employee = employee - 1;
	
							message = " in clear7";
							String clear7 = "update Company set employee='" + employee + "' where id='" + card.getString( "com_id" )  + "';"; 
							// 회사에 등록된 직원이 한명도 없는 경우
							stmt.executeUpdate( clear7 ); 	
						} // end of if-else
						String clear8 = "delete from NonMemberPerson where id='" + card.getString( "id" ) + "';";
						stmt.executeUpdate( clear8 );
					}	
	
				} // cardlist
	
			
				// 디렉토리에 등록된 명함을 삭제한다.
				message = " in query9 ";
				String query9 = "delete from Card where dir_id='" + dir_id + "';";
				stmt.executeUpdate( query9 );	

				// 디렉토리를 삭제한다
				message = " in query10 ";
				String query10 = "delete from Directory where id='" + dir_id + "';";
				stmt.executeUpdate( query10 ); 

			} // end of dirlist

				

			// 다른 사람의 디렉토리에서 내 명함을 지운다.
			message = " in query11 ";
			String query11 = "delete from Card where card_id='" + id + "';";
			stmt.executeUpdate( query11 );

			// MemberPerson에서 아이디를 삭제한다.
			String query12 = "delete from MemberPerson where id='" + id + "';";
			stmt.executeUpdate( query12 ); 

			// commit하고 connection 닫기
			con.commit();
			stmt.close();
			con.close();
		}catch( SQLException e ){
			throw new CommandException( "fail to query  in deleteMemberPerson"  + message + e.toString() );
		}
	}

	/**
	 *  updatePassword  - MemberPerson의 password를 변경하는 method
	 *  
	 *  @param  id        password를 변경할 사용자의 id
	 *  @param  password  새로운 password
	 */
	public void updatePassword( String id, String password ) throws CommandException {

		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	

			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "update MemberPerson set password='" + password + "' where id='" + id + "';" );	
			stmt.executeUpdate( query );	
	
			// connection닫기
			stmt.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to update password "  + e.toString() );	
		} 

	}



	/////////////////////////  NonMemberPerson  //////////////////////

	/**
	 *  getNonMemberPerson  - 개인정보를 넘겨주는 method
	 * 
	 *  @param  id  nonmember 아이디 
	 *
	 *  exception  CommandException  DB와의 연결에 문제가 있을 때 발생하는 exception 
	 */
	public ResultSet getNonMemberPerson( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select * from NonMemberPerson where id='" + id + "';" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to search Company! "  + e.toString());	
		} 
	}

	/**
	 *  getNonMemberInfo  - nonmember 정보를 넘겨주는 method
	 * 
	 *  @param  id   member 아이디 
	 */
	public ResultSet getNonMemberInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	System.err.print( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query =  "select NonMemberPerson.id,  NonMemberPerson.name, Company.name, " + 
					"JobInfo.part, JobInfo.posi  " +
					"from NonMemberPerson, JobInfo, Company " +
					"where NonMemberPerson.id='" + id + 
					"' and Company.id=NonmemberPerson.com_id " +
					"and JobInfo.id=NonMemberPerson.jobinfo;";	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to get NonMemberInfo! "  + e.toString());	
		} 
	}




	/**
	 *	addNonMemberPerson - non memberperson을 NonMemberPerson DB에 추가한다.
	 *
	 *	@param id 			사용자 아이디
	 *	@param name 		사용자 이름
	 *	@param personInfo 	personInfo 아이디
	 *	@param jobInfo 		jobInfo 아이디
	 *	@param com_id 		회사 아이디
	 *
	 */
	public void addNonMemberPerson( String id, String name, String personInfo, String jobInfo, String com_id )throws CommandException{

		Connection con = null;
		Statement stmt = null;

		
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
		   	throw new CommandException( e.toString() );
		}

		//새로운 non member를 DB에 추가한다.
		try{
			//query statement
			String query = "insert into NonMemberPerson (id, name, personinfo, jobInfo, com_id) values ('" + id + "', '" + name + "', '" + personInfo + "', '" + jobInfo + "', '" + com_id + "');";

			//query 문 실행
			stmt.executeUpdate( query );			

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addNonMemberPerson at DBManager " + e.toString() );
		}
	}	



	//////////////////////////  PersonInfo  //////////////////////

	/**
	 *  getPersonInfo  - 개인정보를 넘겨주는 method
	 * 
	 *  @param  id  개인정보 고유 아이디 
	 *
	 *  exception  CommandException  DB와의 연결에 문제가 있을 때 발생하는 exception 
	 */
	public ResultSet getPersonInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			stmt = con.createStatement();
			query = new String( "select * from PersonInfo where id='" + id + "';" );	
			result = stmt.executeQuery( query );	
			
			// connection닫기
			stmt.close();
			con.close();
	
			return result;
		} catch( SQLException e ) {
			throw new CommandException( "fail to  getPersonInfo! "  + e.toString());	
		} 
	}


	/**
	 *	addPersonInfo - person info를 PersonInfo DB에 추가한다.
	 *
	 *	@param id 			personInfo id
	 *	@param address 		사용자 주소( 비가입자일 경우는 회사주소)
	 *	@param tel 			전화번호
	 *	@param beeper 		삐삐번호
	 *	@param handphone 	핸드폰 번호
	 *	@param email 		이메일 주소
	 *	@param homepage 	홈페이지 주소
	 *	@param back 		personInfo link
	 *
	 */
	public void addPersonInfo( String id, String address, String tel, String beeper, String handphone, String email, String homepage, String back )throws CommandException{

		Connection con = null;
		Statement stmt = null;

		
		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
			 
			 
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
		} catch( SQLException e ) {
   			throw new CommandException( e.toString() );
		}

		//새로운 personInfo를 DB에 추가한다.
		try{
			//query statement
			String query = "insert into PersonInfo (id, address, tel, beeper, handphone, email, homepage, back) values ('" + id + "', '" + address + "', '" + tel + "', '" + beeper + "', '" + handphone + "', '" + email + "', '" + homepage + "', '" + back + "');";

			//query 문 실행
			stmt.executeUpdate( query );			

			stmt.close();
			con.close();

		}catch( SQLException e ){
			throw new CommandException( "fail to query " + e.toString() );
		}catch( Exception e ){
			throw new CommandException( "fail to addPersonInfo at DBManager " + e.toString() );
		}
	}

	/**
	 *  deletePersonInfo - user가 가지고 있던 개인정보를 모두 삭제하는 method
	 * 
 	 *  @param  id 최근 개인정보를 아이디
	 *  @throws CommandException DB와의 접속에 문제가 생긴 경우
	 */
	public void deletePersonInfo( String id ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		String query, select;	
		ResultSet result;		 
			 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB " + e.toString() );
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			String person = id;
			select = "select back from PersonInfo where id=? ;";
			query  = "delete PersonInfo where id=? ;";
			stmt1 = con.prepareStatement( select );
			stmt2 = con.prepareStatement( query );
	
			while( !person.equals( "0" ) ) {			
				stmt1.setString( 1, person );
				result = stmt1.executeQuery( ); 	

				// jobinfo를 삭제한다.	
				stmt2.setString( 1, person );	
				if( stmt2.executeUpdate( ) == 0 ){
					// query가 실패한 경우 connection 을 rollback하고 종료한다.
					stmt1.close();
					stmt2.close();
					con.rollback();
					con.close(); 
					throw new CommandException( "fail to delete PersonInfo" );
				}
	
				person = result.getString( "back" ); 
			}

			// commit 한다.
			con.commit();
	
			// connection닫기
			stmt1.close();
			stmt2.close();
			con.close();
	
		} catch( SQLException e ) {
			throw new CommandException( "fail to delete JobInfo! " +e.toString() );	
		} 	
	}

	/**
	 *  updatePersonInfo - 새로운 개인정보를 저장하는  method
	 *
	 *  @param  id  개인정보를 바꿀 사용자의 아이디
	 *  @param  address   새로운 주소
	 *  @param  tel
	 *  @param  beeper
	 *  @param  phone
	 *  @param  email
	 *  @param  homepage
	 */
	public void updatePersonInfo( String id, String address, String tel, String beeper, String phone, String email, String homepage ) throws CommandException {
		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	throw new CommandException( "ClassNotFoundException: " + e.toString() );
		}
	 
		Connection con = null;
		Statement stmt = null;
		String query;	
		ResultSet result = null;		 
		ResultSet member = null;
		ResultSet personInfo = null;
		String current,past;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );

		} catch( SQLException e ) {
			throw new CommandException( " fail to connect DB "  + e.toString());
		}

		// connection에서 statement를 생성해서 query한다.
		try {
			// 현재 등록된 personInfo를 얻는다. 
			member = stmt.executeQuery( "select personinfo from MemberPerson where id='" + id + "';" );
			if( !member.next() ) throw new CommandException( "fail to get MemberPerson "  );
			// 새로 등록될 personInfo에 back으로 등록될 personInfo reference	
			current = member.getString( "personinfo" );
		
			// object생성 시간 입력
			Calendar date = Calendar.getInstance();
		
			String time = String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
		
			String new_personInfo = id + time;	
			//  새로운 personInfo 등록	
			addPersonInfo( new_personInfo, address, tel, beeper, phone, email, homepage, current );

			// MemberPerson에 최신 PersonInfo update	
			if( stmt.executeUpdate( "update MemberPerson set personinfo='" + new_personInfo + "' where id='" + id + "';" )  == 0 ){
				// update가 실패한 경우 rollback하고 connection 을 종료한다.
				stmt.close();	
				con.rollback();
				con.close();			
				throw new CommandException( "fail to update PersonInfo in MemberPerson" );
			}

			// commit 하기
			con.commit();
			// connection닫기
			stmt.close();
			con.close();
		} catch( SQLException e ) {
			throw new CommandException( "fail to update PersonInfo! " + e.toString() );	
		} 	

	}
	
}
