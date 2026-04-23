package Repository;
import dao.NhanVienDao;
import java.util.ArrayList;
import entity.NhanVien;

public class Repository_NhanVien {



    private dao.NhanVienDao dao = new dao.NhanVienDao();
    
    public ArrayList<NhanVien> getAll() {
        return dao.getAllNhanVien();
    }

    public int add(NhanVien nv) {
        return dao.add(nv);
    }

    public int update(NhanVien nv) {
        return dao.update(nv);
    }

    public boolean delete(String maNhanVien) {
        return dao.delete(maNhanVien);
    }

    public ArrayList<NhanVien> searchByPhone(String keyword) {
        return dao.search(keyword);
    }
    
    public ArrayList<NhanVien> getByStatus(int status) {
    return dao.getByStatus(status);
}
    public NhanVien login(String email, String matKhau) {
        return dao.checkLogin(email);
    }
    
    public String getNextMaNhanVien() {
    return dao.getNextMaNhanVien();
}

}
