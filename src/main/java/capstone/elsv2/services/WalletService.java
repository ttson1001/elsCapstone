package capstone.elsv2.services;

import capstone.elsv2.dto.payment.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WalletService {
    Boolean addWallet(AddWalletDTO addWalletDTO);
    ResponseEntity<MomoResponse> addWalletV2(AddWalletDTO addWalletDTO);
    WalletDTO getWallet(String userId);
    WalletDTO getWalletSystem();
    Boolean refund(RefundDTO refundDTO);
    List<TransactionWalletDTO> getAllTransaction(String userId);

}
