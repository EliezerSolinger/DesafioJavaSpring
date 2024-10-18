
function getReadableProjectStatus(status) {
    const statusMap = {
        EM_ANALISE: `<span class="text-primary">Em Análise</span>`,
        ANALISE_REALIZADA: `<span class="text-info">Análise Realizada</span>`,
        ANALISE_APROVADA: `<span class="text-success">Análise Aprovada</span>`,
        INICIADO: `<span class="text-primary">Iniciado</span>`,
        PLANEJADO: `<span class="text-warning">Planejado</span>`,
        EM_ANDAMENTO: `<span class="text-info">Em Andamento</span>`,
        ENCERRADO: `<span class="text-success">Encerrado</span>`,
        CANCELADO: `<span class="text-danger">Cancelado</span>`
    };

    return statusMap[status] || `<span class="text-secondary">Status Desconhecido</span>`;
}

// VIEWW
async function reloadProjetosTable(showSpinner=true) { 
    let tabela=$("#tabela-projetos");

    if(showSpinner) {
        tabela.html(
            `<tr><td colspan="6"><div class="text-center">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">Carregando...</span>
                </div>
                <p class="mt-2">Carregando...</p>
            </div></td></tr>`);
    }  
    let projetosArr=await fetchProjetos();
    if(projetosArr.length==0) { 
        tabela.html(
            `<tr><td colspan="6"><div class="text-center"> 
                <p class="mt-2">Nenhum projeto cadastrado.</p>
            </div></td></tr>`); 
            
        return
    }
    
    let html='';
    projetosArr.forEach(projeto => {
        let gerente=false;
        if(projeto.gerente) gerente=projeto.gerente.nome;
        html+=`
            <tr>
                <td>${projeto.id}</td>
                <td>${projeto.nome}</td>
                <td>${gerente || 'N/A'}</td>
                <td>${getReadableProjectStatus(projeto.status)}</td>
                <td>${projeto.riskClassification || 'N/A'}</td>
                <td class="text-end">
                    <button class="btn btn-outline-dark btn-sm" onclick="openFuncionariosModal(${projeto.id})">
                        <i class="fa-solid fa-users"></i> Membros
                    </button>
                    <button class="btn btn-secondary btn-sm" onclick="openEditProject(${projeto.id})">
                        <i class="fa-solid fa-edit"></i> 
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDeleteProject(${projeto.id})">
                        <i class="fa-solid fa-trash"></i> 
                    </button>
                </td>
            </tr>
        `; 
    });
    tabela.html(html);
}
async function reloadGerenteSelectbox() {
    let selectbox=$('#gerentesSelect');
    // Limpa a lista existente e adiciona uma opção padrão
     selectbox.html('<option value="" disabled selected>Selecione um gerente</option>');
    
     let gerentes = await fetchGerentes();
     // Mapeia os gerentes e cria as novas opções
     const options = gerentes.map(gerente => 
         `<option value="${gerente.id}">${gerente.nome}</option>`
     ).join('');
     selectbox.append(options);
}

async function openProjectModal(title,projetoObject) {
    await reloadGerenteSelectbox(); 
    $("#projetoModalTitle").html(title);
    let projetoModal=$("#projetoModal");  
    const parseDate=strdate=> (strdate ? strdate.split('T')[0] : undefined);
    projetoModal.find("[name=id]").val(projetoObject.id); 
    projetoModal.find("[name=nome]").val(projetoObject.nome);
    projetoModal.find("[name=dataInicio]").val(parseDate(projetoObject.dataInicio));
    projetoModal.find("[name=dataPrevisaoFim]").val(parseDate(projetoObject.dataPrevisaoFim));
    projetoModal.find("[name=dataFim]").val(parseDate(projetoObject.dataFim));
    projetoModal.find("[name=descricao]").val(projetoObject.descricao);
    projetoModal.find("[name=status]").val(projetoObject.status);
    projetoModal.find("[name=orcamento]").val(projetoObject.orcamento); 
    let gerenteId=0;
    if(projetoObject.gerente) gerenteId=projetoObject.gerente.id; 
    projetoModal.find("[name=gerente]").val(gerenteId);  
    projetoModal.modal('show');
}

function openNewProject() {
    let projetoObject={id:0};
    openProjectModal("Novo Projeto",projetoObject);
}
async function openEditProject(projectId) {
    let projetoObject=await fetchProjeto(projectId);
    openProjectModal("Editar Projeto",projetoObject); 
}

async function reloadFuncionariosSelectbox(members = []) {
    let selectbox=$('#funcionariosSelect');
    // Limpa a lista existente e adiciona uma opção padrão
     selectbox.html('<option value="" disabled selected>Selecione um funcionario</option>');
    
     let funcionario = await fetchFuncionarios();
     
     funcionario = funcionario.filter(f => {
        // Verifica se existe algum membro com o mesmo ID
        const existeNoMembro = members.some(m => m.id === f.id);
        // Retorna false para remover da lista, true para manter
        return !existeNoMembro;
    });
     // Mapeia os funcionario e cria as novas opções
     const options = funcionario.map(funcionario => 
         `<option value="${funcionario.id}">${funcionario.nome}</option>`
     ).join('');
     selectbox.append(options);
}
async function reloadFuncionariosTable(projectId,showSpinner=true) { 
    let tabela=$("#tabela-funcionarios");

    if(showSpinner) {
        tabela.html(
            `<tr><td colspan="6"><div class="text-center">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">Carregando...</span>
                </div>
                <p class="mt-2">Carregando...</p>
            </div></td></tr>`);
    }   
    let arr=await fetchProjectMembers(projectId);
    reloadFuncionariosSelectbox(arr);
    if(arr.length==0) { 
        tabela.html(
            `<tr><td colspan="6"><div class="text-center"> 
                <p class="mt-2">Nenhum membro associado ao projeto.</p>
            </div></td></tr>`); 
            
        return
    }
    
    let html='';
    arr.forEach(membro => { 
        html+=`
            <tr>
                <td class="px-3">${membro.id}</td>
                <td>${membro.nome}</td>   
                <td class="text-end"> 
                    <button class="btn btn-outline-danger btn-sm" onclick="removeProjectMemberAndReload(${projectId},${membro.id})">
                        <i class="fa-solid fa-times"></i> 
                    </button>
                </td>
            </tr>
        `; 
    });
    tabela.html(html);


}
async function removeProjectMemberAndReload(projectId,pessoaId) {
    await removeProjectMember(projectId,pessoaId);
    await reloadFuncionariosTable(projectId,false);    
}
async function addProjectMemberAndReload(projectId,pessoaId) {
    await addProjectMember(projectId,pessoaId);
    await reloadFuncionariosTable(projectId,false);    
}
async function openFuncionariosModal(projectId) {
    await reloadFuncionariosTable(projectId);
    $("#btnAddMember").off('click').click(function () {
        let selectedMember=$("#funcionariosSelect").val();
        if(!selectedMember) return;
        addProjectMemberAndReload(projectId,selectedMember);
    });
      
    $("#funcionariosModal").modal('show');
}

// VIEWW
async function reloadPessoasTable(showSpinner=true) { 
    let tabela=$("#tabela-pessoas");

    if(showSpinner) {
        tabela.html(
            `<tr><td colspan="6"><div class="text-center">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">Carregando...</span>
                </div>
                <p class="mt-2">Carregando...</p>
            </div></td></tr>`);
    }  
    let pessoasArr=await fetchPessoas();
    if(pessoasArr.length==0) { 
        tabela.html(
            `<tr><td colspan="6"><div class="text-center"> 
                <p class="mt-2">Nenhum membro cadastrado.</p>
            </div></td></tr>`); 
            
        return;
    }

    
    let html = '';
    pessoasArr.forEach(pessoa => {
        let cargo = "";
        if (pessoa.gerente) {
            cargo = `<span class="badge bg-primary">GERENTE</span>`;
        } else {
            cargo = `<span class="badge bg-secondary">FUNCIONÁRIO</span>`;
        }
    
        // Formata a data de nascimento no padrão dd/mm/yyyy
        const dataNascimento = pessoa.dataNascimento 
            ? new Date(pessoa.dataNascimento).toLocaleDateString('pt-BR') 
            : 'N/A';
    
        html += `
            <tr>
                <td>${pessoa.id}</td>
                <td>${pessoa.nome}</td>
                <td class="text-center">${cargo}</td> 
                <td>${pessoa.cpf || 'N/A'}</td>
                <td>${dataNascimento}</td>
            </tr>
        `;
    });
    
    tabela.html(html);
}
function getProjectFormObject() {
    const formatarDataISO = (data) => data ? new Date(data).toISOString() : null;
    const form = $('#form-projeto');

    const gerenteId = parseInt(form.find('[name="gerente"]').val());
    const idProjeto = form.find('[name="id"]').val();

    return {
        ...(idProjeto ? { id: idProjeto } : {}), // Adiciona o ID apenas se existir
        nome: form.find('[name="nome"]').val(),
        dataInicio: formatarDataISO(form.find('[name="dataInicio"]').val()),
        dataPrevisaoFim: formatarDataISO(form.find('[name="dataPrevisaoFim"]').val()),
        dataFim: formatarDataISO(form.find('[name="dataFim"]').val()),
        descricao: form.find('[name="descricao"]').val(),
        status: form.find('[name="status"]').val(),
        orcamento: parseFloat(form.find('[name="orcamento"]').val()) || 0,
        gerente: {
            id: !isNaN(gerenteId) && gerenteId > 0 ? gerenteId : null // Define `null` se gerente for inválido
        }
    };
}

async function confirmDeleteProject(projectId) {
    if(confirm('Tem certeza que deseja deletar o projeto?')) {
        let response=await deleteProject(projectId);
        if(response.status==400) {
            alert(await response.text())
        }
        console.log(response);
        await reloadProjetosTable();
    }
}
// CONTROLLER 
async function appStart() {
    reloadProjetosTable();
    reloadPessoasTable();

    $("#form-projeto").submit(function(e) {
        e.preventDefault();
        (async() => {
            await saveProject(getProjectFormObject());
            reloadProjetosTable();
        })();
        $("#projetoModal").modal('hide');
        return false;
    });
} 
 

document.addEventListener('DOMContentLoaded', appStart);