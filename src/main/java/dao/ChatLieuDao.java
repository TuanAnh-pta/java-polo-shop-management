package dao;

import entity.ChatLieu;
import java.rmi.server.ObjID;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class ChatLieuDao {
    String getAllSql = """
                        SELECT [MaChatLieu]
                              ,[ChatLieu]
                             
                          FROM [QLBanAoPoLo_01_12].[dbo].[ChatLieu]
                       """;
    public List<ChatLieu> getAll(){
    return XQuery.getBeanList(ChatLieu.class, getAllSql);
    }
    String findByIdSql = """
                         SELECT [MaChatLieu]
                               ,[ChatLieu]
                              
                           FROM [QLBanAoPoLo_01_12].[dbo].[ChatLieu]
                           WHERE [MaChatLieu] = ?
                         """;
    public ChatLieu finById(String MaChatLieu){
    return XQuery.getSingleBean(ChatLieu.class, findByIdSql, MaChatLieu);
    }
    String createSql = """
                       INSERT INTO [dbo].[ChatLieu]
                                  ([MaChatLieu]
                                  ,[ChatLieu]
                                  )
                            VALUES
                                  (?,?)
                       """;
    public int create(ChatLieu chatLieu){
        Object[] value={
        chatLieu.getMaChatLieu(),
        chatLieu.getChatLieu()
        
       };
       return XJdbc.executeUpdate(createSql, value);
    }
    
    String deleteSql = """
                       DELETE FROM [dbo].[ChatLieu]
                             WHERE MaChatLieu = ?
                       """;
    public int delete(String ma){
     return XJdbc.executeUpdate(deleteSql,ma);
    }
    String updateSql = """
                       UPDATE [dbo].[ChatLieu]
                          SET [ChatLieu] = ?
                             
                        WHERE [MaChatLieu] = ?
                       """;
    public int update(ChatLieu chatLieu){
        Object[] value={   
          chatLieu.getChatLieu(),
          chatLieu.getMaChatLieu()
        };
        return XJdbc.executeUpdate(updateSql, value);
    }
    String getByTen = """
                      select * from ChatLieu where ChatLieu like ?
                      """;
    public List<ChatLieu> getByTen(String ten){
     return XQuery.getBeanList(ChatLieu.class,getByTen,"%"+ten.toLowerCase()+"%");
    }
}
