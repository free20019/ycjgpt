package helper;
import java.sql.Types;

import org.hibernate.Hibernate;

public class MyDialect  extends org.hibernate.dialect.OracleDialect{
	public MyDialect(){
		super();
		this.registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
	}
}
