import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";

const FilmTable = ({data}) => {
  return (
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Title</TableCell>
            <TableCell>Released On</TableCell>
            <TableCell>Director</TableCell>
            <TableCell>Characters</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.result.map((film, index) => (
            <TableRow key={index}>
              <TableCell>{film.title}</TableCell>
              <TableCell>{film.releaseDate}</TableCell>
              <TableCell>{film.director}</TableCell>
              <TableCell>{film.peopleList?.map(people => people.name).join(", ")}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default FilmTable;