package kosanku.service;

import java.util.List;
import java.util.Optional;
import kosanku.dao.TenantDAO;
import kosanku.model.Tenant;

public class TenantService {
    private final TenantDAO tenantDAO;

    public TenantService(TenantDAO tenantDAO) {
        if (tenantDAO == null) {
            throw new IllegalArgumentException("TenantDAO wajib diisi.");
        }
        this.tenantDAO = tenantDAO;
    }

    public List<Tenant> getAll() {
        return tenantDAO.findAll();
    }

    public Optional<Tenant> getById(int id) {
        validateId(id);
        return tenantDAO.findById(id);
    }

    public void create(Tenant tenant) {
        validate(tenant);
        ensureUnique(tenant, null);
        if (!tenantDAO.insert(tenant)) {
            throw new IllegalStateException("Gagal menambahkan penghuni.");
        }
    }

    public void edit(Tenant tenant) {
        validate(tenant);
        validateId(tenant.getId());
        ensureUnique(tenant, tenant.getId());
        if (!tenantDAO.update(tenant)) {
            throw new IllegalStateException("Data penghuni tidak ditemukan atau gagal diubah.");
        }
    }

    public void delete(int id) {
        validateId(id);
        if (tenantDAO.hasVisibleContract(id)) {
            throw new IllegalStateException(
                "Penghuni masih memiliki kontrak aktif. Akhiri kontrak terlebih dahulu."
            );
        }

        boolean deleted = tenantDAO.hasContracts(id)
            ? tenantDAO.deactivate(id)
            : tenantDAO.delete(id);
        if (!deleted) {
            throw new IllegalStateException("Penghuni tidak ditemukan atau gagal dihapus.");
        }
    }

    private void ensureUnique(Tenant tenant, Integer currentId) {
        Optional<Tenant> matchingIdNumber = tenantDAO.findByIdNumber(tenant.getIdNumber());
        if (isOtherTenant(matchingIdNumber, currentId)) {
            throw new IllegalArgumentException("NIK sudah terdaftar.");
        }

        if (tenant.getEmail() != null && !tenant.getEmail().isBlank()) {
            Optional<Tenant> matchingEmail = tenantDAO.findByEmail(tenant.getEmail());
            if (isOtherTenant(matchingEmail, currentId)) {
                throw new IllegalArgumentException("Email sudah terdaftar.");
            }
        }
    }

    private boolean isOtherTenant(Optional<Tenant> match, Integer currentId) {
        return match.isPresent() && (currentId == null || match.get().getId() != currentId);
    }

    private void validate(Tenant tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("Data penghuni wajib diisi.");
        }
        tenant.setIdNumber(trimToNull(tenant.getIdNumber()));
        tenant.setName(trimToNull(tenant.getName()));
        tenant.setEmail(trimToNull(tenant.getEmail()));
        tenant.setPhoneNumber(trimToNull(tenant.getPhoneNumber()));

        if (tenant.getIdNumber() == null) {
            throw new IllegalArgumentException("NIK wajib diisi.");
        }
        if (tenant.getIdNumber().length() > 20) {
            throw new IllegalArgumentException("NIK maksimal 20 karakter.");
        }
        if (tenant.getName() == null) {
            throw new IllegalArgumentException("Nama wajib diisi.");
        }
        if (tenant.getName().length() > 100) {
            throw new IllegalArgumentException("Nama maksimal 100 karakter.");
        }
        if (tenant.getEmail() != null && tenant.getEmail().length() > 100) {
            throw new IllegalArgumentException("Email maksimal 100 karakter.");
        }
        if (tenant.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Nomor telepon wajib diisi.");
        }
        if (tenant.getPhoneNumber().length() > 20) {
            throw new IllegalArgumentException("Nomor telepon maksimal 20 karakter.");
        }
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return value.trim();
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID penghuni tidak valid.");
        }
    }
}
