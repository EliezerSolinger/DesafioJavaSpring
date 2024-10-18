/// DATA 
const API_URL = 'http://localhost:8080/api';

async function fetchAPI(route) {
    return await (await fetch(API_URL+route)).json();
}

function sendAPI(route,jsonData,method='POST') { 
    return fetch(API_URL + route, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })  
}
function postAPI(route,jsonData) {
    return sendAPI(route,jsonData,'POST');
} 
function putAPI(route,jsonData) {
    return sendAPI(route,jsonData,'PUT');
} 
function deleteAPI(route,jsonData) {
    return sendAPI(route,jsonData,'DELETE');
}


async function saveProject(projectObject) {
    try{
        if(projectObject.id && parseInt(projectObject.id)!=0) {
            // update info
            return await putAPI('/projetos/'+projectObject.id,projectObject);
        } else {
            return await postAPI('/projetos',projectObject);
        }
    } catch(error) {
        alert('ERRO ao salvar/cadastrar projeto! consulte os logs.' + error);
        console.log(error);
        return {};
    }
}

async function deleteProject(projectId) {
    try{  
        return await deleteAPI('/projetos/'+projectId); 
    } catch(error) {
        alert('ERRO ao apagar o projeto! consulte os logs.' + error);
        console.log(error);
        return {};
    }
}
async function fetchProjetos() {
    return await fetchAPI("/projetos");
}
async function fetchProjeto(projectId) {
    return await fetchAPI("/projetos/"+projectId);
}
async function fetchPessoas() {
    return await fetchAPI("/pessoas");
}
async function fetchFuncionarios() {
    return (await fetchPessoas()).filter(p=>! p.gerente);
} 
async function fetchGerentes() {
    return (await fetchPessoas()).filter(p=>p.gerente);
}
async function fetchPessoa(pessoaId) {
    return await fetchAPI("/pessoas/"+pessoaId);
}
async function fetchProjectMembers(projetoId) { 
    return (await fetchProjeto(projetoId)).membros;
}
async function removeProjectMember(projectId,pessoaId) {
    return await deleteAPI(`/projetos/${projectId}/members`,{id:pessoaId});
}
async function addProjectMember(projectId,pessoaId) {
    return await postAPI(`/projetos/${projectId}/members`,{id:pessoaId});
}