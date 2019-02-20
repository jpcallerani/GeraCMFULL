package Telas;

import geracmfull.MDI_CM;
import java.awt.Cursor;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import loader.TrOracleConnection;
import objetos.Schema;

public class frmGeradorFULL extends javax.swing.JInternalFrame {

    private TrOracleConnection con;
    private GridLayout v_gridlayout_g;

    public frmGeradorFULL() {
        initComponents();
        setFrameIcon(new ImageIcon("images/favicon.png"));

        jPanelProdutos.setLayout(new java.awt.GridLayout(1, 1));

        jPanelProdutos.removeAll();
        jPanelProdutos.repaint();
        jPanelProdutos.revalidate();

        // recupera as informações dos demais produtos.
        v_gridlayout_g = (GridLayout) jPanelProdutos.getLayout();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelConteudo = new javax.swing.JPanel();
        jPanelBase = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jTextFieldSenha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTNS = new javax.swing.JTextField();
        jLabelLoader = new javax.swing.JLabel();
        jButtonAdicionar = new javax.swing.JButton();
        jButtonAdicionar1 = new javax.swing.JButton();
        jButtonAdicionar2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelProdutos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();

        setClosable(true);
        setTitle("Gerador de FULL");
        setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanelBase.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informações da Base", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Unicode", 0, 12), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jLabel2.setText("Usuário");

        jLabel3.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jLabel3.setText("Senha");

        jTextFieldUsuario.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N

        jTextFieldSenha.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jLabel4.setText("TNS");

        jTextFieldTNS.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanelBaseLayout = new javax.swing.GroupLayout(jPanelBase);
        jPanelBase.setLayout(jPanelBaseLayout);
        jPanelBaseLayout.setHorizontalGroup(
            jPanelBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTNS, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBaseLayout.setVerticalGroup(
            jPanelBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldTNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelLoader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loader_disable.png"))); // NOI18N

        jButtonAdicionar.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jButtonAdicionar.setText("Adicionar");
        jButtonAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonAdicionarMousePressed(evt);
            }
        });

        jButtonAdicionar1.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jButtonAdicionar1.setText("Limpar");
        jButtonAdicionar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonAdicionar1MousePressed(evt);
            }
        });

        jButtonAdicionar2.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jButtonAdicionar2.setText("Gerar FULL");
        jButtonAdicionar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonAdicionar2MousePressed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        jPanelProdutos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produtos Selecionados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Unicode", 0, 12), new java.awt.Color(255, 153, 0))); // NOI18N

        javax.swing.GroupLayout jPanelProdutosLayout = new javax.swing.GroupLayout(jPanelProdutos);
        jPanelProdutos.setLayout(jPanelProdutosLayout);
        jPanelProdutosLayout.setHorizontalGroup(
            jPanelProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 915, Short.MAX_VALUE)
        );
        jPanelProdutosLayout.setVerticalGroup(
            jPanelProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanelProdutos);

        jTextAreaLog.setEditable(false);
        jTextAreaLog.setColumns(20);
        jTextAreaLog.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 12)); // NOI18N
        jTextAreaLog.setRows(5);
        jScrollPane2.setViewportView(jTextAreaLog);

        javax.swing.GroupLayout jPanelConteudoLayout = new javax.swing.GroupLayout(jPanelConteudo);
        jPanelConteudo.setLayout(jPanelConteudoLayout);
        jPanelConteudoLayout.setHorizontalGroup(
            jPanelConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConteudoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelLoader, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelConteudoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanelBase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConteudoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConteudoLayout.createSequentialGroup()
                                .addComponent(jButtonAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonAdicionar1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonAdicionar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanelConteudoLayout.setVerticalGroup(
            jPanelConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConteudoLayout.createSequentialGroup()
                .addComponent(jPanelBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdicionar)
                    .addComponent(jButtonAdicionar1))
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAdicionar2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelLoader, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelConteudo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelConteudo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        MDI_CM.frmGeradorFULL = null;
    }//GEN-LAST:event_formInternalFrameClosed

    private void jButtonAdicionarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAdicionarMousePressed
        Thread v_thread_install = new Thread("Tr_Adicionar") {
            @Override
            public void run() {
                //
                // Coloca a tela em estado de espera.
                //
                setLoader();
                desabilitaComponentes();
                //
                // Cria a tela de processamento dos pacotes.
                //
                try {
                    con = new TrOracleConnection();
                    con.setUsername(jTextFieldUsuario.getText());
                    con.setPassword(jTextFieldSenha.getText());
                    con.setTns(jTextFieldTNS.getText());
                    con.Connect();
                    con.Close();

                    frmSchemas frmschema = new frmSchemas(new Schema(jTextFieldUsuario.getText(), jTextFieldSenha.getText(), jTextFieldTNS.getText()));

                    // adiciona cada produto no painel existente.
                    jPanelProdutos.add(frmschema);
                    v_gridlayout_g.setRows(v_gridlayout_g.getRows() + 1);

                    jPanelProdutos.repaint();
                    jPanelProdutos.revalidate();

                    jTextFieldUsuario.setText("");
                    jTextFieldSenha.setText("");

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Usuário/Senha@TNS não existe.", "Atenção", JOptionPane.WARNING_MESSAGE);
                }

                //
                // Volta a tela no formato inicial.
                //
                setLoaderDisables();
                habilitaComponentes();
            }
        };
        v_thread_install.start();
    }//GEN-LAST:event_jButtonAdicionarMousePressed

    private void jButtonAdicionar1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAdicionar1MousePressed
        jPanelProdutos.removeAll();
        jPanelProdutos.repaint();
        jPanelProdutos.revalidate();
        jTextFieldUsuario.setText("");
        jTextFieldSenha.setText("");
        jTextFieldTNS.setText("");
    }//GEN-LAST:event_jButtonAdicionar1MousePressed

    private void jButtonAdicionar2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAdicionar2MousePressed
        if (v_gridlayout_g.getRows() == 0) {
            JOptionPane.showConfirmDialog(null, "Nenhum owner selecionado.");
        } else {
            for (int i = 0; i < jPanelProdutos.getComponentCount(); i++) {
                frmSchemas schemas = (frmSchemas) jPanelProdutos.getComponents()[i];
                
            }
        }
    }//GEN-LAST:event_jButtonAdicionar2MousePressed

    /**
     *
     * @return
     */
    private void setLoaderDisables() {
        jLabelLoader.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Images/loader_disable.png")));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     *
     * @return
     */
    private void setLoader() {
        jLabelLoader.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Images/loader.gif")));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    /**
     *
     */
    private void desabilitaComponentes() {
        jButtonAdicionar.setEnabled(false);
        for (int i = 0; i < jPanelBase.getComponentCount(); i++) {
            Object object = jPanelBase.getComponent(i);

            if (object instanceof JTextField) {
                ((JTextField) object).setEnabled(false);
            }
        }
    }

    /**
     *
     */
    private void habilitaComponentes() {
        jButtonAdicionar.setEnabled(true);
        for (int i = 0; i < jPanelBase.getComponentCount(); i++) {
            Object object = jPanelBase.getComponent(i);

            if (object instanceof JTextField) {
                ((JTextField) object).setEnabled(true);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdicionar;
    private javax.swing.JButton jButtonAdicionar1;
    private javax.swing.JButton jButtonAdicionar2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelLoader;
    private javax.swing.JPanel jPanelBase;
    private javax.swing.JPanel jPanelConteudo;
    private javax.swing.JPanel jPanelProdutos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextFieldSenha;
    private javax.swing.JTextField jTextFieldTNS;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
