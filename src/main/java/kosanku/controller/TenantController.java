package kosanku.controller;

import java.util.List;
import java.util.Optional;
import kosanku.model.Tenant;
import kosanku.service.TenantService;

public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        if (tenantService == null) {
            throw new IllegalArgumentException("TenantService wajib diisi.");
        }
        this.tenantService = tenantService;
    }

    public List<Tenant> loadAll() {
        return tenantService.getAll();
    }

    public Optional<Tenant> findById(int id) {
        return tenantService.getById(id);
    }

    public void create(String idNumber, String name, String email, String phoneNumber) {
        tenantService.create(buildTenant(null, idNumber, name, email, phoneNumber));
    }

    public void edit(int id, String idNumber, String name, String email, String phoneNumber) {
        tenantService.edit(buildTenant(id, idNumber, name, email, phoneNumber));
    }

    public void delete(int id) {
        tenantService.delete(id);
    }

    private Tenant buildTenant(Integer id, String idNumber, String name, String email, String phoneNumber) {
        Tenant tenant = new Tenant(
            idNumber,
            name,
            email,
            phoneNumber
        );
        if (id != null) tenant.setId(id);
        return tenant;
    }
}
