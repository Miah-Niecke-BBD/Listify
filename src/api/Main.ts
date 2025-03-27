
export function removeJwt(){
    localStorage.removeItem("jwtToken"); 
    window.location.href = "/";
}
